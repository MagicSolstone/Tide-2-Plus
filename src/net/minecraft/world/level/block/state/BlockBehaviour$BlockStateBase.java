package net.minecraft.world.level.block.state;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos$MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.TypedInstance;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Block$UpdateFlags;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.level.storage.loot.LootParams$Builder;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public abstract class BlockBehaviour$BlockStateBase extends StateHolder<Block, BlockState> implements TypedInstance<Block> {
   private static final Direction[] DIRECTIONS = Direction.values();
   private static final VoxelShape[] EMPTY_OCCLUSION_SHAPES = Util.make(new VoxelShape[DIRECTIONS.length], s -> Arrays.fill(s, Shapes.empty()));
   private static final VoxelShape[] FULL_BLOCK_OCCLUSION_SHAPES = Util.make(new VoxelShape[DIRECTIONS.length], s -> Arrays.fill(s, Shapes.block()));
   private final int lightEmission;
   private final boolean useShapeForLightOcclusion;
   private final boolean isAir;
   private final boolean ignitedByLava;
   @Deprecated
   private final boolean liquid;
   @Deprecated
   private boolean legacySolid;
   private final PushReaction pushReaction;
   private final MapColor mapColor;
   private final float destroySpeed;
   private final boolean requiresCorrectToolForDrops;
   private final boolean canOcclude;
   private final BlockBehaviour$StatePredicate isRedstoneConductor;
   private final BlockBehaviour$StatePredicate isSuffocating;
   private final BlockBehaviour$StateArgumentPredicate<AABB> isViewBlocking;
   private final BlockBehaviour$PostProcess postProcess;
   private final Predicate<BlockState> emissiveRendering;
   @Nullable
   private final BlockBehaviour$OffsetFunction offsetFunction;
   private final boolean spawnTerrainParticles;
   private final NoteBlockInstrument instrument;
   private final boolean replaceable;
   @Nullable
   private BlockBehaviour$BlockStateBase$Cache cache;
   private FluidState fluidState = Fluids.EMPTY.defaultFluidState();
   private boolean isRandomlyTicking;
   private boolean solidRender;
   private VoxelShape occlusionShape;
   private VoxelShape[] occlusionShapesByFace;
   private boolean propagatesSkylightDown;
   private int lightDampening;

   protected BlockBehaviour$BlockStateBase(final Block owner, final Property<?>[] propertyKeys, final Comparable<?>[] propertyValues) {
      super(owner, propertyKeys, propertyValues);
      BlockBehaviour$Properties properties = owner.properties;
      this.lightEmission = properties.lightEmission.applyAsInt(this.asState());
      this.useShapeForLightOcclusion = owner.useShapeForLightOcclusion(this.asState());
      this.isAir = properties.isAir;
      this.ignitedByLava = properties.ignitedByLava;
      this.liquid = properties.liquid;
      this.pushReaction = properties.pushReaction;
      this.mapColor = properties.mapColor.apply(this.asState());
      this.destroySpeed = properties.destroyTime;
      this.requiresCorrectToolForDrops = properties.requiresCorrectToolForDrops;
      this.canOcclude = properties.canOcclude;
      this.isRedstoneConductor = properties.isRedstoneConductor;
      this.isSuffocating = properties.isSuffocating;
      this.isViewBlocking = properties.isViewBlocking;
      this.postProcess = properties.postProcess;
      this.emissiveRendering = properties.emissiveRendering;
      this.offsetFunction = properties.offsetFunction;
      this.spawnTerrainParticles = properties.spawnTerrainParticles;
      this.instrument = properties.instrument;
      this.replaceable = properties.replaceable;
   }

   private boolean calculateSolid() {
      if (this.owner.properties.forceSolidOn) {
         return SolidDebugger.logAndGet(this.typeHolder(), SolidDebugger$Reason.FORCE_SOLID_ON);
      } else if (this.owner.properties.forceSolidOff) {
         return SolidDebugger.logAndGet(this.typeHolder(), SolidDebugger$Reason.FORCE_SOLID_OFF);
      } else if (this.cache == null) {
         return SolidDebugger.logAndGet(this.typeHolder(), SolidDebugger$Reason.NULL_CACHE);
      } else {
         VoxelShape shape = this.cache.collisionShape;
         if (shape.isEmpty()) {
            return SolidDebugger.logAndGet(this.typeHolder(), SolidDebugger$Reason.EMPTY_COLLISION_SHAPE);
         } else {
            AABB bounds = shape.bounds();
            if (bounds.getSize() >= 0.7291666666666666) {
               return SolidDebugger.logAndGet(this.typeHolder(), SolidDebugger$Reason.LARGE_ENOUGH_COLLISION_SHAPE);
            } else {
               return bounds.getYsize() >= 1.0
                  ? SolidDebugger.logAndGet(this.typeHolder(), SolidDebugger$Reason.HIGH_ENOUGH_COLLISION_SHAPE)
                  : SolidDebugger.logAndGet(this.typeHolder(), SolidDebugger$Reason.FALLTHROUGH);
            }
         }
      }
   }

   public void initCache() {
      this.fluidState = this.owner.getFluidState(this.asState());
      this.isRandomlyTicking = this.owner.isRandomlyTicking(this.asState());
      if (!this.getBlock().hasDynamicShape()) {
         this.cache = new BlockBehaviour$BlockStateBase$Cache(this.asState());
      }

      this.legacySolid = this.calculateSolid();
      this.occlusionShape = this.canOcclude ? this.owner.getOcclusionShape(this.asState()) : Shapes.empty();
      this.solidRender = Block.isShapeFullBlock(this.occlusionShape);
      if (this.occlusionShape.isEmpty()) {
         this.occlusionShapesByFace = EMPTY_OCCLUSION_SHAPES;
      } else if (this.solidRender) {
         this.occlusionShapesByFace = FULL_BLOCK_OCCLUSION_SHAPES;
      } else {
         this.occlusionShapesByFace = new VoxelShape[DIRECTIONS.length];

         for (Direction direction : DIRECTIONS) {
            this.occlusionShapesByFace[direction.ordinal()] = this.occlusionShape.getFaceShape(direction);
         }
      }

      this.propagatesSkylightDown = this.owner.propagatesSkylightDown(this.asState());
      this.lightDampening = this.owner.getLightDampening(this.asState());
   }

   public Block getBlock() {
      return this.owner;
   }

   @Override
   public Holder<Block> typeHolder() {
      return this.getBlock().builtInRegistryHolder();
   }

   @Deprecated
   public boolean isSolid() {
      return this.legacySolid;
   }

   public boolean isValidSpawn(final BlockGetter level, final BlockPos pos, final EntityType<?> type) {
      return this.getBlock().properties.isValidSpawn.test(this.asState(), level, pos, type);
   }

   public boolean propagatesSkylightDown() {
      return this.propagatesSkylightDown;
   }

   public int getLightDampening() {
      return this.lightDampening;
   }

   public VoxelShape getFaceOcclusionShape(final Direction direction) {
      return this.occlusionShapesByFace[direction.ordinal()];
   }

   public VoxelShape getOcclusionShape() {
      return this.occlusionShape;
   }

   public boolean hasLargeCollisionShape() {
      return this.cache == null || this.cache.largeCollisionShape;
   }

   public boolean useShapeForLightOcclusion() {
      return this.useShapeForLightOcclusion;
   }

   public int getLightEmission() {
      return this.lightEmission;
   }

   public boolean isAir() {
      return this.isAir;
   }

   public boolean ignitedByLava() {
      return this.ignitedByLava;
   }

   @Deprecated
   public boolean liquid() {
      return this.liquid;
   }

   public MapColor getMapColor(final BlockGetter level, final BlockPos pos) {
      return this.mapColor;
   }

   public BlockState rotate(final Rotation rotation) {
      return this.getBlock().rotate(this.asState(), rotation);
   }

   public BlockState mirror(final Mirror mirror) {
      return this.getBlock().mirror(this.asState(), mirror);
   }

   public RenderShape getRenderShape() {
      return this.getBlock().getRenderShape(this.asState());
   }

   public boolean emissiveRendering() {
      return this.emissiveRendering.test(this.asState());
   }

   public float getShadeBrightness(final BlockGetter level, final BlockPos pos) {
      return this.getBlock().getShadeBrightness(this.asState(), level, pos);
   }

   public boolean isRedstoneConductor(final BlockGetter level, final BlockPos pos) {
      return this.isRedstoneConductor.test(this.asState(), level, pos);
   }

   public boolean isSignalSource() {
      return this.getBlock().isSignalSource(this.asState());
   }

   public boolean shouldRedstoneWireConnectTo(final BlockGetter level, final BlockPos pos, @Nullable final Direction direction) {
      return this.getBlock().shouldRedstoneWireConnectTo(this.asState(), level, pos, direction);
   }

   public int getOwnSignal(final BlockGetter level, final BlockPos pos) {
      return this.getBlock().ownSignal(this.asState(), level, pos);
   }

   public int getSignal(final BlockGetter level, final BlockPos pos, final Direction direction) {
      return this.getBlock().getSignal(this.asState(), level, pos, direction);
   }

   public boolean hasAnalogOutputSignal() {
      return this.getBlock().hasAnalogOutputSignal(this.asState());
   }

   public int getAnalogOutputSignal(final Level level, final BlockPos pos, final Direction direction) {
      return this.getBlock().getAnalogOutputSignal(this.asState(), level, pos, direction);
   }

   public float getDestroySpeed(final BlockGetter level, final BlockPos pos) {
      return this.destroySpeed;
   }

   public float getDestroyProgress(final Player player, final BlockGetter level, final BlockPos pos) {
      return this.getBlock().getDestroyProgress(this.asState(), player, level, pos);
   }

   public int getDirectSignal(final BlockGetter level, final BlockPos pos, final Direction direction) {
      return this.getBlock().getDirectSignal(this.asState(), level, pos, direction);
   }

   public PushReaction getPistonPushReaction() {
      return this.pushReaction;
   }

   public boolean isSolidRender() {
      return this.solidRender;
   }

   public boolean canOcclude() {
      return this.canOcclude;
   }

   public boolean skipRendering(final BlockState neighborState, final Direction direction) {
      return this.getBlock().skipRendering(this.asState(), neighborState, direction);
   }

   public VoxelShape getShape(final BlockGetter level, final BlockPos pos) {
      return this.getShape(level, pos, CollisionContext.empty());
   }

   public VoxelShape getShape(final BlockGetter level, final BlockPos pos, final CollisionContext context) {
      return this.getBlock().getShape(this.asState(), level, pos, context);
   }

   public VoxelShape getCollisionShape(final BlockGetter level, final BlockPos pos) {
      return this.cache != null ? this.cache.collisionShape : this.getCollisionShape(level, pos, CollisionContext.empty());
   }

   public VoxelShape getCollisionShape(final BlockGetter level, final BlockPos pos, final CollisionContext context) {
      return this.getBlock().getCollisionShape(this.asState(), level, pos, context);
   }

   public VoxelShape getEntityInsideCollisionShape(final BlockGetter level, final BlockPos pos, final Entity entity) {
      return this.getBlock().getEntityInsideCollisionShape(this.asState(), level, pos, entity);
   }

   public VoxelShape getBlockSupportShape(final BlockGetter level, final BlockPos pos) {
      return this.getBlock().getBlockSupportShape(this.asState(), level, pos);
   }

   public VoxelShape getVisualShape(final BlockGetter level, final BlockPos pos, final CollisionContext context) {
      return this.getBlock().getVisualShape(this.asState(), level, pos, context);
   }

   public VoxelShape getInteractionShape(final BlockGetter level, final BlockPos pos) {
      return this.getBlock().getInteractionShape(this.asState(), level, pos);
   }

   public final boolean entityCanStandOn(final BlockGetter level, final BlockPos pos, final Entity entity) {
      return this.entityCanStandOnFace(level, pos, entity, Direction.UP);
   }

   public final boolean entityCanStandOnFace(final BlockGetter level, final BlockPos pos, final Entity entity, final Direction faceDirection) {
      return Block.isFaceFull(this.getCollisionShape(level, pos, CollisionContext.of(entity)), faceDirection);
   }

   public Vec3 getOffset(final BlockPos pos) {
      BlockBehaviour$OffsetFunction function = this.offsetFunction;
      return function != null ? function.evaluate(this.asState(), pos) : Vec3.ZERO;
   }

   public boolean hasOffsetFunction() {
      return this.offsetFunction != null;
   }

   public boolean triggerEvent(final Level level, final BlockPos pos, final int b0, final int b1) {
      return this.getBlock().triggerEvent(this.asState(), level, pos, b0, b1);
   }

   public void handleNeighborChanged(
      final Level level, final BlockPos pos, final Block block, @Nullable final Orientation orientation, final boolean movedByPiston
   ) {
      this.getBlock().neighborChanged(this.asState(), level, pos, block, orientation, movedByPiston);
   }

   public final void updateNeighbourShapes(final LevelAccessor level, final BlockPos pos, @Block$UpdateFlags final int updateFlags) {
      this.updateNeighbourShapes(level, pos, updateFlags, 512);
   }

   public final void updateNeighbourShapes(final LevelAccessor level, final BlockPos pos, @Block$UpdateFlags final int updateFlags, final int updateLimit) {
      BlockPos$MutableBlockPos blockPos = new BlockPos$MutableBlockPos();

      for (Direction direction : BlockBehaviour.UPDATE_SHAPE_ORDER) {
         blockPos.setWithOffset(pos, direction);
         level.neighborShapeChanged(direction.getOpposite(), blockPos, pos, this.asState(), updateFlags, updateLimit);
      }
   }

   public final void updateIndirectNeighbourShapes(final LevelAccessor level, final BlockPos pos, @Block$UpdateFlags final int updateFlags) {
      this.updateIndirectNeighbourShapes(level, pos, updateFlags, 512);
   }

   public void updateIndirectNeighbourShapes(final LevelAccessor level, final BlockPos pos, @Block$UpdateFlags final int updateFlags, final int updateLimit) {
      this.getBlock().updateIndirectNeighbourShapes(this.asState(), level, pos, updateFlags, updateLimit);
   }

   public void onPlace(final Level level, final BlockPos pos, final BlockState oldState, final boolean movedByPiston) {
      this.getBlock().onPlace(this.asState(), level, pos, oldState, movedByPiston);
   }

   public void affectNeighborsAfterRemoval(final ServerLevel level, final BlockPos pos, final boolean movedByPiston) {
      this.getBlock().affectNeighborsAfterRemoval(this.asState(), level, pos, movedByPiston);
   }

   public void onExplosionHit(final ServerLevel level, final BlockPos pos, final Explosion explosion, final BiConsumer<ItemStack, BlockPos> onHit) {
      this.getBlock().onExplosionHit(this.asState(), level, pos, explosion, onHit);
   }

   public void tick(final ServerLevel level, final BlockPos pos, final RandomSource random) {
      this.getBlock().tick(this.asState(), level, pos, random);
   }

   public void randomTick(final ServerLevel level, final BlockPos pos, final RandomSource random) {
      this.getBlock().randomTick(this.asState(), level, pos, random);
   }

   public void entityInside(final Level level, final BlockPos pos, final Entity entity, final InsideBlockEffectApplier effectApplier, final boolean isPrecise) {
      this.getBlock().entityInside(this.asState(), level, pos, entity, effectApplier, isPrecise);
   }

   public void spawnAfterBreak(final ServerLevel level, final BlockPos pos, final ItemStack tool, final boolean dropExperience) {
      this.getBlock().spawnAfterBreak(this.asState(), level, pos, tool, dropExperience);
   }

   public List<ItemStack> getDrops(final LootParams$Builder params) {
      return this.getBlock().getDrops(this.asState(), params);
   }

   public InteractionResult useItemOn(
      final ItemStack itemStack, final Level level, final Player player, final InteractionHand hand, final BlockHitResult hitResult
   ) {
      return this.getBlock().useItemOn(itemStack, this.asState(), level, hitResult.getBlockPos(), player, hand, hitResult);
   }

   public InteractionResult useWithoutItem(final Level level, final Player player, final BlockHitResult hitResult) {
      return this.getBlock().useWithoutItem(this.asState(), level, hitResult.getBlockPos(), player, hitResult);
   }

   public boolean showAsInteractableInSpectatorMode(final Level level, final BlockPos pos, final BlockHitResult hitResult) {
      return this.getBlock().showAsInteractableInSpectatorMode(this.asState(), level, pos, hitResult);
   }

   public void attack(final Level level, final BlockPos pos, final Player player) {
      this.getBlock().attack(this.asState(), level, pos, player);
   }

   public boolean isSuffocating(final BlockGetter level, final BlockPos pos) {
      return this.isSuffocating.test(this.asState(), level, pos);
   }

   public boolean isLightPermeable() {
      return !this.solidRender || this.getLightDampening() == 0;
   }

   public boolean isViewBlocking(final BlockGetter level, final BlockPos blockPos, final AABB nearPlaneBox) {
      return this.isViewBlocking.test(this.asState(), level, blockPos, nearPlaneBox);
   }

   public BlockState updateShape(
      final LevelReader level,
      final ScheduledTickAccess ticks,
      final BlockPos pos,
      final Direction directionToNeighbour,
      final BlockPos neighbourPos,
      final BlockState neighbourState,
      final RandomSource random
   ) {
      return this.getBlock().updateShape(this.asState(), level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
   }

   public boolean isPathfindable(final PathComputationType type) {
      return this.getBlock().isPathfindable(this.asState(), type);
   }

   public boolean canBeReplaced(final BlockPlaceContext context) {
      return this.getBlock().canBeReplaced(this.asState(), context);
   }

   public boolean canBeReplaced(final Fluid fluid) {
      return this.getBlock().canBeReplaced(this.asState(), fluid);
   }

   public boolean canBeReplaced() {
      return this.replaceable;
   }

   public boolean canSurvive(final LevelReader level, final BlockPos pos) {
      return this.getBlock().canSurvive(this.asState(), level, pos);
   }

   @Nullable
   public BlockPos getPostProcessPos(final BlockGetter level, final BlockPos pos) {
      return this.postProcess.getPostProcessPos(this.asState(), level, pos);
   }

   @Nullable
   public MenuProvider getMenuProvider(final Level level, final BlockPos pos) {
      return this.getBlock().getMenuProvider(this.asState(), level, pos);
   }

   public boolean is(final TagKey<Block> tag, final Predicate<BlockBehaviour$BlockStateBase> predicate) {
      return this.is(tag) && predicate.test(this);
   }

   public boolean hasBlockEntity() {
      return this.getBlock() instanceof EntityBlock;
   }

   public boolean shouldChangedStateKeepBlockEntity(final BlockState oldState) {
      return this.getBlock().shouldChangedStateKeepBlockEntity(oldState);
   }

   @Nullable
   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(final Level level, final BlockEntityType<T> type) {
      return this.getBlock() instanceof EntityBlock ? ((EntityBlock)this.getBlock()).getTicker(level, this.asState(), type) : null;
   }

   public FluidState getFluidState() {
      return this.fluidState;
   }

   public boolean isRandomlyTicking() {
      return this.isRandomlyTicking;
   }

   public long getSeed(final BlockPos pos) {
      return this.getBlock().getSeed(this.asState(), pos);
   }

   public SoundType getSoundType() {
      return this.getBlock().getSoundType(this.asState());
   }

   public void onProjectileHit(final Level level, final BlockState state, final BlockHitResult blockHit, final Projectile entity) {
      this.getBlock().onProjectileHit(level, state, blockHit, entity);
   }

   public boolean isFaceSturdy(final BlockGetter level, final BlockPos pos, final Direction direction) {
      return this.isFaceSturdy(level, pos, direction, SupportType.FULL);
   }

   public boolean isFaceSturdy(final BlockGetter level, final BlockPos pos, final Direction direction, final SupportType supportType) {
      return this.cache != null ? this.cache.isFaceSturdy(direction, supportType) : supportType.isSupporting(this.asState(), level, pos, direction);
   }

   public boolean isCollisionShapeFullBlock(final BlockGetter level, final BlockPos pos) {
      return this.cache != null ? this.cache.isCollisionShapeFullBlock : this.getBlock().isCollisionShapeFullBlock(this.asState(), level, pos);
   }

   public ItemStack getCloneItemStack(final LevelReader level, final BlockPos pos, final boolean includeData) {
      return this.getBlock().getCloneItemStack(level, pos, this.asState(), includeData);
   }

   protected abstract BlockState asState();

   public boolean requiresCorrectToolForDrops() {
      return this.requiresCorrectToolForDrops;
   }

   public boolean shouldSpawnTerrainParticles() {
      return this.spawnTerrainParticles;
   }

   public BlockState withPropertiesOf(final BlockState source) {
      BlockState result = this.asState();

      for (Property<?> property : source.getBlock().getStateDefinition().getProperties()) {
         result = copyProperty(source, result, property);
      }

      return result;
   }

   public static <T extends Comparable<T>> BlockState copyProperty(final BlockState from, final BlockState to, final Property<T> property) {
      return to.trySetValue(property, from.getValue(property));
   }

   public NoteBlockInstrument instrument() {
      return this.instrument;
   }
}
