package net.minecraft.world.level.block.state;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Explosion$BlockInteraction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Block$UpdateFlags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Portal;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootParams$Builder;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public abstract class BlockBehaviour implements FeatureElement {
   protected static final Direction[] UPDATE_SHAPE_ORDER = new Direction[]{
      Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH, Direction.DOWN, Direction.UP
   };
   protected final boolean hasCollision;
   protected final float explosionResistance;
   protected final boolean isRandomlyTicking;
   protected final SoundType soundType;
   protected final float friction;
   protected final float speedFactor;
   protected final float jumpFactor;
   protected final float bounceRestitution;
   protected final float fallDistanceReduction;
   protected final boolean dynamicShape;
   protected final FeatureFlagSet requiredFeatures;
   protected final BlockBehaviour$Properties properties;
   protected final Optional<ResourceKey<LootTable>> drops;
   protected final String descriptionId;

   public BlockBehaviour(final BlockBehaviour$Properties properties) {
      this.hasCollision = properties.hasCollision;
      this.drops = properties.effectiveDrops();
      this.descriptionId = properties.effectiveDescriptionId();
      this.explosionResistance = properties.explosionResistance;
      this.isRandomlyTicking = properties.isRandomlyTicking;
      this.soundType = properties.soundType;
      this.friction = properties.friction;
      this.speedFactor = properties.speedFactor;
      this.jumpFactor = properties.jumpFactor;
      this.bounceRestitution = properties.bounceRestitution;
      this.fallDistanceReduction = properties.fallDistanceReduction;
      this.dynamicShape = properties.dynamicShape;
      this.requiredFeatures = properties.requiredFeatures;
      this.properties = properties;
   }

   public BlockBehaviour$Properties properties() {
      return this.properties;
   }

   protected void updateIndirectNeighbourShapes(
      final BlockState state, final LevelAccessor level, final BlockPos pos, @Block$UpdateFlags final int updateFlags, final int updateLimit
   ) {
   }

   protected boolean isPathfindable(final BlockState state, final PathComputationType type) {
      return switch (type) {
         case LAND -> !state.isCollisionShapeFullBlock(EmptyBlockGetter.INSTANCE, BlockPos.ZERO);
         case WATER -> state.getFluidState().is(FluidTags.WATER);
         case AIR -> !state.isCollisionShapeFullBlock(EmptyBlockGetter.INSTANCE, BlockPos.ZERO);
         default -> false;
      };
   }

   protected BlockState updateShape(
      final BlockState state,
      final LevelReader level,
      final ScheduledTickAccess ticks,
      final BlockPos pos,
      final Direction directionToNeighbour,
      final BlockPos neighbourPos,
      final BlockState neighbourState,
      final RandomSource random
   ) {
      return state;
   }

   protected boolean skipRendering(final BlockState state, final BlockState neighborState, final Direction direction) {
      return false;
   }

   protected void neighborChanged(
      final BlockState state, final Level level, final BlockPos pos, final Block block, @Nullable final Orientation orientation, final boolean movedByPiston
   ) {
   }

   protected void onPlace(final BlockState state, final Level level, final BlockPos pos, final BlockState oldState, final boolean movedByPiston) {
   }

   protected void affectNeighborsAfterRemoval(final BlockState state, final ServerLevel level, final BlockPos pos, final boolean movedByPiston) {
   }

   protected void onExplosionHit(
      final BlockState state, final ServerLevel level, final BlockPos pos, final Explosion explosion, final BiConsumer<ItemStack, BlockPos> onHit
   ) {
      if (!state.isAir() && explosion.getBlockInteraction() != Explosion$BlockInteraction.TRIGGER_BLOCK) {
         Block block = state.getBlock();
         boolean doDropExperienceHack = explosion.getIndirectSourceEntity() instanceof Player;
         if (block.dropFromExplosion(explosion)) {
            BlockEntity blockEntity = state.hasBlockEntity() ? level.getBlockEntity(pos) : null;
            LootParams$Builder params = new LootParams$Builder(level)
               .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
               .withParameter(LootContextParams.TOOL, ItemStack.EMPTY)
               .withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockEntity)
               .withOptionalParameter(LootContextParams.THIS_ENTITY, explosion.getDirectSourceEntity());
            if (explosion.getBlockInteraction() == Explosion$BlockInteraction.DESTROY_WITH_DECAY) {
               params.withParameter(LootContextParams.EXPLOSION_RADIUS, explosion.radius());
            }

            state.spawnAfterBreak(level, pos, ItemStack.EMPTY, doDropExperienceHack);
            state.getDrops(params).forEach(stack -> onHit.accept(stack, pos));
         }

         level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
         block.wasExploded(level, pos, explosion);
      }
   }

   protected InteractionResult useWithoutItem(
      final BlockState state, final Level level, final BlockPos pos, final Player player, final BlockHitResult hitResult
   ) {
      return InteractionResult.PASS;
   }

   protected InteractionResult useItemOn(
      final ItemStack itemStack,
      final BlockState state,
      final Level level,
      final BlockPos pos,
      final Player player,
      final InteractionHand hand,
      final BlockHitResult hitResult
   ) {
      return InteractionResult.TRY_WITH_EMPTY_HAND;
   }

   protected boolean showAsInteractableInSpectatorMode(final BlockState state, final Level level, final BlockPos pos, final BlockHitResult hitResult) {
      return state.getMenuProvider(level, pos) != null || state.getBlock() instanceof Portal;
   }

   protected boolean triggerEvent(final BlockState state, final Level level, final BlockPos pos, final int b0, final int b1) {
      return false;
   }

   protected RenderShape getRenderShape(final BlockState state) {
      return RenderShape.MODEL;
   }

   protected boolean useShapeForLightOcclusion(final BlockState state) {
      return false;
   }

   protected FluidState getFluidState(final BlockState state) {
      return Fluids.EMPTY.defaultFluidState();
   }

   protected boolean hasAnalogOutputSignal(final BlockState state) {
      return false;
   }

   protected float getMaxHorizontalOffset() {
      return 0.25F;
   }

   protected float getMaxVerticalOffset() {
      return 0.2F;
   }

   @Override
   public FeatureFlagSet requiredFeatures() {
      return this.requiredFeatures;
   }

   protected boolean shouldChangedStateKeepBlockEntity(final BlockState oldState) {
      return false;
   }

   protected BlockState rotate(final BlockState state, final Rotation rotation) {
      return state;
   }

   protected BlockState mirror(final BlockState state, final Mirror mirror) {
      return state;
   }

   protected boolean canBeReplaced(final BlockState state, final BlockPlaceContext context) {
      return state.canBeReplaced() && (context.getItemInHand().isEmpty() || !context.getItemInHand().is(this.asItem()));
   }

   protected boolean canBeReplaced(final BlockState state, final Fluid fluid) {
      return state.canBeReplaced() || !state.isSolid();
   }

   protected List<ItemStack> getDrops(final BlockState state, final LootParams$Builder params) {
      if (this.drops.isEmpty()) {
         return Collections.emptyList();
      } else {
         LootParams lootParams = params.withParameter(LootContextParams.BLOCK_STATE, state).create(LootContextParamSets.BLOCK);
         ServerLevel level = lootParams.getLevel();
         LootTable table = level.getServer().reloadableRegistries().getLootTable(this.drops.get());
         return table.getRandomItems(lootParams);
      }
   }

   protected long getSeed(final BlockState state, final BlockPos pos) {
      return Mth.getSeed(pos);
   }

   protected VoxelShape getOcclusionShape(final BlockState state) {
      return state.getShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO);
   }

   protected VoxelShape getBlockSupportShape(final BlockState state, final BlockGetter level, final BlockPos pos) {
      return this.getCollisionShape(state, level, pos, CollisionContext.empty());
   }

   protected VoxelShape getInteractionShape(final BlockState state, final BlockGetter level, final BlockPos pos) {
      return Shapes.empty();
   }

   protected int getLightDampening(final BlockState state) {
      if (state.isSolidRender()) {
         return 15;
      } else {
         return state.propagatesSkylightDown() ? 0 : 1;
      }
   }

   @Nullable
   protected MenuProvider getMenuProvider(final BlockState state, final Level level, final BlockPos pos) {
      return null;
   }

   protected boolean canSurvive(final BlockState state, final LevelReader level, final BlockPos pos) {
      return true;
   }

   protected float getShadeBrightness(final BlockState state, final BlockGetter level, final BlockPos pos) {
      return state.isCollisionShapeFullBlock(level, pos) ? 0.2F : 1.0F;
   }

   protected int getAnalogOutputSignal(final BlockState state, final Level level, final BlockPos pos, final Direction direction) {
      return 0;
   }

   protected VoxelShape getShape(final BlockState state, final BlockGetter level, final BlockPos pos, final CollisionContext context) {
      return Shapes.block();
   }

   protected VoxelShape getCollisionShape(final BlockState state, final BlockGetter level, final BlockPos pos, final CollisionContext context) {
      return this.hasCollision ? state.getShape(level, pos) : Shapes.empty();
   }

   protected VoxelShape getEntityInsideCollisionShape(final BlockState state, final BlockGetter level, final BlockPos pos, final Entity entity) {
      return Shapes.block();
   }

   protected boolean isCollisionShapeFullBlock(final BlockState state, final BlockGetter level, final BlockPos pos) {
      return Block.isShapeFullBlock(state.getCollisionShape(level, pos));
   }

   protected VoxelShape getVisualShape(final BlockState state, final BlockGetter level, final BlockPos pos, final CollisionContext context) {
      return this.getCollisionShape(state, level, pos, context);
   }

   protected void randomTick(final BlockState state, final ServerLevel level, final BlockPos pos, final RandomSource random) {
   }

   protected void tick(final BlockState state, final ServerLevel level, final BlockPos pos, final RandomSource random) {
   }

   protected float getDestroyProgress(final BlockState state, final Player player, final BlockGetter level, final BlockPos pos) {
      float destroySpeed = state.getDestroySpeed(level, pos);
      if (destroySpeed == -1.0F) {
         return 0.0F;
      } else {
         int modifier = player.hasCorrectToolForDrops(state) ? 30 : 100;
         return player.getDestroySpeed(state) / destroySpeed / modifier;
      }
   }

   protected void spawnAfterBreak(final BlockState state, final ServerLevel level, final BlockPos pos, final ItemStack tool, final boolean dropExperience) {
   }

   protected void attack(final BlockState state, final Level level, final BlockPos pos, final Player player) {
   }

   protected boolean isSignalSource(final BlockState state) {
      return false;
   }

   protected boolean shouldRedstoneWireConnectTo(final BlockState state, final BlockGetter level, final BlockPos pos, @Nullable final Direction direction) {
      return state.isSignalSource() && direction != null;
   }

   protected int getSignal(final BlockState state, final BlockGetter level, final BlockPos pos, final Direction direction) {
      return this.ownSignal(state, level, pos);
   }

   protected int ownSignal(final BlockState state, final BlockGetter level, final BlockPos pos) {
      return 0;
   }

   protected void entityInside(
      final BlockState state, final Level level, final BlockPos pos, final Entity entity, final InsideBlockEffectApplier effectApplier, final boolean isPrecise
   ) {
   }

   protected int getDirectSignal(final BlockState state, final BlockGetter level, final BlockPos pos, final Direction direction) {
      return 0;
   }

   public final Optional<ResourceKey<LootTable>> getLootTable() {
      return this.drops;
   }

   public final String getDescriptionId() {
      return this.descriptionId;
   }

   protected void onProjectileHit(final Level level, final BlockState state, final BlockHitResult blockHit, final Projectile projectile) {
   }

   protected boolean propagatesSkylightDown(final BlockState state) {
      return !Block.isShapeFullBlock(state.getShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO)) && state.getFluidState().isEmpty();
   }

   protected boolean isRandomlyTicking(final BlockState state) {
      return this.isRandomlyTicking;
   }

   protected SoundType getSoundType(final BlockState state) {
      return this.soundType;
   }

   protected ItemStack getCloneItemStack(final LevelReader level, final BlockPos pos, final BlockState state, final boolean includeData) {
      return new ItemStack(this.asItem());
   }

   public abstract Item asItem();

   protected abstract Block asBlock();

   public MapColor defaultMapColor() {
      return this.properties.mapColor.apply(this.asBlock().defaultBlockState());
   }

   public float defaultDestroyTime() {
      return this.properties.destroyTime;
   }
}
