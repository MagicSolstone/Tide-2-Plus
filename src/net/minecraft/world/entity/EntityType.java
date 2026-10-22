package net.minecraft.world.entity;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction$Axis;
import net.minecraft.core.Holder$Reference;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.ProblemReporter$ScopedCollector;
import net.minecraft.util.Util;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueInput$ValueInputList;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public class EntityType<T extends Entity> implements EntityTypeTest<Entity, T>, FeatureElement {
   private static final Logger LOGGER = LogUtils.getLogger();
   private final Holder$Reference<EntityType<?>> builtInRegistryHolder = BuiltInRegistries.ENTITY_TYPE.createIntrusiveHolder(this);
   public static final Codec<EntityType<?>> CODEC = BuiltInRegistries.ENTITY_TYPE.byNameCodec();
   public static final StreamCodec<RegistryFriendlyByteBuf, EntityType<?>> STREAM_CODEC = ByteBufCodecs.registry(Registries.ENTITY_TYPE);
   public static final int NO_UPDATE_INTERVAL = Integer.MAX_VALUE;
   private final EntityType$EntityFactory<T> factory;
   private final MobCategory category;
   private final TagKey<Block> immuneTo;
   private final boolean serialize;
   private final boolean summon;
   private final boolean fireImmune;
   private final boolean canSpawnFarFromPlayer;
   private final int clientTrackingRange;
   private final int updateInterval;
   private final String descriptionId;
   @Nullable
   private Component description;
   private final Optional<ResourceKey<LootTable>> lootTable;
   private final EntityDimensions dimensions;
   private final float spawnDimensionsScale;
   private final FeatureFlagSet requiredFeatures;
   private final boolean allowedInPeaceful;
   private final boolean trackDeltas;

   public static Identifier getKey(final EntityType<?> type) {
      return BuiltInRegistries.ENTITY_TYPE.getKey(type);
   }

   public EntityType(
      final EntityType$EntityFactory<T> factory,
      final MobCategory category,
      final boolean serialize,
      final boolean summon,
      final boolean fireImmune,
      final boolean canSpawnFarFromPlayer,
      final TagKey<Block> immuneTo,
      final EntityDimensions dimensions,
      final float spawnDimensionsScale,
      final int clientTrackingRange,
      final int updateInterval,
      final String descriptionId,
      final Optional<ResourceKey<LootTable>> lootTable,
      final FeatureFlagSet requiredFeatures,
      final boolean allowedInPeaceful,
      final boolean trackDeltas
   ) {
      this.factory = factory;
      this.category = category;
      this.canSpawnFarFromPlayer = canSpawnFarFromPlayer;
      this.serialize = serialize;
      this.summon = summon;
      this.fireImmune = fireImmune;
      this.immuneTo = immuneTo;
      this.dimensions = dimensions;
      this.spawnDimensionsScale = spawnDimensionsScale;
      this.clientTrackingRange = clientTrackingRange;
      this.updateInterval = updateInterval;
      this.descriptionId = descriptionId;
      this.lootTable = lootTable;
      this.requiredFeatures = requiredFeatures;
      this.allowedInPeaceful = allowedInPeaceful;
      this.trackDeltas = trackDeltas;
   }

   @Nullable
   public T spawn(
      final ServerLevel level,
      @Nullable final ItemStack itemStack,
      @Nullable final LivingEntity user,
      final BlockPos spawnPos,
      final EntitySpawnReason spawnReason,
      final boolean tryMoveDown,
      final boolean movedUp
   ) {
      PostSpawnProcessor<T> postSpawnConfig;
      if (itemStack != null) {
         postSpawnConfig = createDefaultStackConfig(level, itemStack, user);
      } else {
         postSpawnConfig = PostSpawnProcessor.nop();
      }

      return this.spawn(level, postSpawnConfig, spawnPos, spawnReason, tryMoveDown, movedUp);
   }

   public static <T extends Entity> PostSpawnProcessor<T> createDefaultStackConfig(
      final Level level, final ItemStack itemStack, @Nullable final LivingEntity user
   ) {
      return appendDefaultStackConfig(PostSpawnProcessor.nop(), level, itemStack, user);
   }

   public static <T extends Entity> PostSpawnProcessor<T> appendDefaultStackConfig(
      final PostSpawnProcessor<T> initialConfig, final Level level, final ItemStack itemStack, @Nullable final LivingEntity user
   ) {
      return appendCustomEntityStackConfig(appendComponentsConfig(initialConfig, itemStack), level, itemStack, user);
   }

   public static <T extends Entity> PostSpawnProcessor<T> appendComponentsConfig(final PostSpawnProcessor<T> initialConfig, final ItemStack itemStack) {
      return initialConfig.andThen(entity -> entity.applyComponentsFromItemStack(itemStack));
   }

   public static <T extends Entity> PostSpawnProcessor<T> appendCustomEntityStackConfig(
      final PostSpawnProcessor<T> initialConfig, final Level level, final ItemStack itemStack, @Nullable final LivingEntity user
   ) {
      TypedEntityData<EntityType<?>> entityData = itemStack.get(DataComponents.ENTITY_DATA);
      return entityData != null ? initialConfig.andThen(entity -> updateCustomEntityTag(level, user, entity, entityData)) : initialConfig;
   }

   @Nullable
   public T spawn(final ServerLevel level, final BlockPos spawnPos, final EntitySpawnReason spawnReason) {
      return this.spawn(level, null, spawnPos, spawnReason, false, false);
   }

   @Nullable
   public T spawn(
      final ServerLevel level,
      @Nullable final PostSpawnProcessor<T> postSpawnConfig,
      final BlockPos spawnPos,
      final EntitySpawnReason spawnReason,
      final boolean tryMoveDown,
      final boolean movedUp
   ) {
      T entity = this.create(level, postSpawnConfig, spawnPos, spawnReason, tryMoveDown, movedUp);
      if (entity != null) {
         level.addFreshEntityWithPassengers(entity);
         if (entity instanceof Mob mob) {
            mob.playAmbientSound();
         }
      }

      return entity;
   }

   @Nullable
   public T create(
      final ServerLevel level,
      @Nullable final PostSpawnProcessor<T> postSpawnConfig,
      final BlockPos spawnPos,
      final EntitySpawnReason spawnReason,
      final boolean tryMoveDown,
      final boolean movedUp
   ) {
      T entity = this.create(level, spawnReason);
      if (entity == null) {
         return null;
      } else {
         double yOff;
         if (tryMoveDown) {
            entity.setPos(spawnPos.getX() + 0.5, spawnPos.getY() + 1, spawnPos.getZ() + 0.5);
            yOff = getYOffset(level, spawnPos, movedUp, entity.getBoundingBox());
         } else {
            yOff = 0.0;
         }

         entity.snapTo(spawnPos.getX() + 0.5, spawnPos.getY() + yOff, spawnPos.getZ() + 0.5, Mth.wrapDegrees(level.getRandom().nextFloat() * 360.0F), 0.0F);
         if (entity instanceof Mob mob) {
            mob.yHeadRot = mob.getYRot();
            mob.yBodyRot = mob.getYRot();
            mob.finalizeSpawn(level, level.getCurrentDifficultyAt(mob.blockPosition()), spawnReason, null);
         }

         if (postSpawnConfig != null) {
            postSpawnConfig.apply(entity);
         }

         return entity;
      }
   }

   protected static double getYOffset(final LevelReader level, final BlockPos spawnPos, final boolean movedUp, final AABB entityBox) {
      AABB aabb = new AABB(spawnPos);
      if (movedUp) {
         aabb = aabb.expandTowards(0.0, -1.0, 0.0);
      }

      Iterable<VoxelShape> shapes = level.getCollisions(null, aabb);
      return 1.0 + Shapes.collide(Direction$Axis.Y, entityBox, shapes, movedUp ? -2.0 : -1.0);
   }

   public static void updateCustomEntityTag(
      final Level level, @Nullable final LivingEntity user, @Nullable final Entity entity, final TypedEntityData<EntityType<?>> entityData
   ) {
      MinecraftServer server = level.getServer();
      if (server != null && entity != null) {
         if (entity.getType() == entityData.type()) {
            if (level.isClientSide() || !entity.getType().onlyOpCanSetNbt() || user instanceof Player player && server.getPlayerList().isOp(player.nameAndId())
               )
             {
               entityData.loadInto(entity);
            }
         }
      }
   }

   public boolean canSerialize() {
      return this.serialize;
   }

   public boolean canSummon() {
      return this.summon;
   }

   public boolean fireImmune() {
      return this.fireImmune;
   }

   public boolean canSpawnFarFromPlayer() {
      return this.canSpawnFarFromPlayer;
   }

   public MobCategory getCategory() {
      return this.category;
   }

   public String getDescriptionId() {
      return this.descriptionId;
   }

   public Component getDescription() {
      if (this.description == null) {
         this.description = Component.translatable(this.getDescriptionId());
      }

      return this.description;
   }

   @Override
   public String toString() {
      return this.getDescriptionId();
   }

   public String toShortString() {
      int dot = this.getDescriptionId().lastIndexOf(46);
      return dot == -1 ? this.getDescriptionId() : this.getDescriptionId().substring(dot + 1);
   }

   public Optional<ResourceKey<LootTable>> getDefaultLootTable() {
      return this.lootTable;
   }

   public float getWidth() {
      return this.dimensions.width();
   }

   public float getHeight() {
      return this.dimensions.height();
   }

   @Override
   public FeatureFlagSet requiredFeatures() {
      return this.requiredFeatures;
   }

   public boolean canSpawn(final Level level) {
      return !this.isEnabled(level.enabledFeatures()) ? false : this.isAllowedInPeaceful() || level.getDifficulty() != Difficulty.PEACEFUL;
   }

   @Nullable
   public T create(final Level level, final EntitySpawnReason reason) {
      return this.create(level, new EntitySpawnRequest(reason, false));
   }

   @Nullable
   public T create(final Level level, final EntitySpawnRequest request) {
      return !request.ignoreChecks() && !this.canSpawn(level) ? null : this.factory.create(this, level);
   }

   public static Optional<Entity> create(final ValueInput input, final Level level, final EntitySpawnRequest request) {
      return Util.ifElse(
         by(input).map(type -> type.create(level, request)),
         entity -> entity.load(input),
         () -> LOGGER.warn("Skipping Entity with id {}", input.getStringOr("id", "[invalid]"))
      );
   }

   public static Optional<Entity> create(final EntityType<?> type, final ValueInput input, final Level level, final EntitySpawnReason reason) {
      Optional<Entity> entity = Optional.ofNullable(type.create(level, reason));
      entity.ifPresent(e -> e.load(input));
      return entity;
   }

   public AABB getSpawnAABB(final Vec3 at) {
      return this.getSpawnAABB(at.x, at.y, at.z);
   }

   public AABB getSpawnAABB(final double x, final double y, final double z) {
      float halfWidth = this.spawnDimensionsScale * this.getWidth() / 2.0F;
      float height = this.spawnDimensionsScale * this.getHeight();
      return new AABB(x - halfWidth, y, z - halfWidth, x + halfWidth, y + height, z + halfWidth);
   }

   public boolean isBlockDangerous(final BlockState state) {
      if (state.is(this.immuneTo)) {
         return false;
      } else {
         return !this.fireImmune && NodeEvaluator.isBurningBlock(state)
            ? true
            : state.is(Blocks.WITHER_ROSE) || state.is(Blocks.SWEET_BERRY_BUSH) || state.is(Blocks.CACTUS) || state.is(Blocks.POWDER_SNOW);
      }
   }

   public EntityDimensions getDimensions() {
      return this.dimensions;
   }

   public static Optional<EntityType<?>> by(final ValueInput input) {
      return input.read("id", CODEC);
   }

   @Nullable
   public static Entity loadEntityRecursive(final CompoundTag tag, final Level level, final EntitySpawnRequest request, final EntityProcessor postLoad) {
      Entity var5;
      try (ProblemReporter$ScopedCollector reporter = new ProblemReporter$ScopedCollector(LOGGER)) {
         var5 = loadEntityRecursive(TagValueInput.create(reporter, level.registryAccess(), tag), level, request, postLoad);
      }

      return var5;
   }

   @Nullable
   public static Entity loadEntityRecursive(
      final EntityType<?> type, final CompoundTag tag, final Level level, final EntitySpawnReason reason, final EntityProcessor postLoad
   ) {
      Entity var6;
      try (ProblemReporter$ScopedCollector reporter = new ProblemReporter$ScopedCollector(LOGGER)) {
         var6 = loadEntityRecursive(type, TagValueInput.create(reporter, level.registryAccess(), tag), level, reason, postLoad);
      }

      return var6;
   }

   @Nullable
   public static Entity loadEntityRecursive(final ValueInput input, final Level level, final EntitySpawnReason reason, final EntityProcessor postLoad) {
      return loadEntityRecursive(input, level, new EntitySpawnRequest(reason, false), postLoad);
   }

   @Nullable
   public static Entity loadEntityRecursive(final ValueInput input, final Level level, final EntitySpawnRequest request, final EntityProcessor postLoad) {
      return loadStaticEntity(input, level, request)
         .map(postLoad::process)
         .map(entity -> loadPassengersRecursive(entity, input, level, request, postLoad))
         .orElse(null);
   }

   @Nullable
   public static Entity loadEntityRecursive(
      final EntityType<?> type, final ValueInput input, final Level level, final EntitySpawnReason reason, final EntityProcessor postLoad
   ) {
      return loadStaticEntity(type, input, level, reason)
         .map(postLoad::process)
         .map(entity -> loadPassengersRecursive(entity, input, level, new EntitySpawnRequest(reason, false), postLoad))
         .orElse(null);
   }

   private static Entity loadPassengersRecursive(
      final Entity entity, final ValueInput input, final Level level, final EntitySpawnRequest request, final EntityProcessor postLoad
   ) {
      for (ValueInput passengerTag : input.childrenListOrEmpty("Passengers")) {
         Entity passenger = loadEntityRecursive(passengerTag, level, request, postLoad);
         if (passenger != null) {
            passenger.startRiding(entity, true, false);
         }
      }

      return entity;
   }

   public static Stream<Entity> loadEntitiesRecursive(final ValueInput$ValueInputList entities, final Level level, final EntitySpawnReason reason) {
      return entities.stream().mapMulti((tag, output) -> loadEntityRecursive(tag, level, reason, entity -> {
         output.accept(entity);
         return entity;
      }));
   }

   private static Optional<Entity> loadStaticEntity(final ValueInput input, final Level level, final EntitySpawnRequest request) {
      try {
         return create(input, level, request);
      } catch (RuntimeException var4) {
         LOGGER.warn("Exception loading entity: ", var4);
         return Optional.empty();
      }
   }

   private static Optional<Entity> loadStaticEntity(final EntityType<?> type, final ValueInput input, final Level level, final EntitySpawnReason reason) {
      try {
         return create(type, input, level, reason);
      } catch (RuntimeException var5) {
         LOGGER.warn("Exception loading entity: ", var5);
         return Optional.empty();
      }
   }

   public int clientTrackingRange() {
      return this.clientTrackingRange;
   }

   public int updateInterval() {
      return this.updateInterval;
   }

   public boolean hasUpdateInterval() {
      return this.updateInterval != Integer.MAX_VALUE;
   }

   public boolean trackDeltas() {
      return this.trackDeltas;
   }

   @Nullable
   public T tryCast(final Entity entity) {
      return (T)(entity.getType() == this ? entity : null);
   }

   @Override
   public Class<? extends Entity> getBaseClass() {
      return Entity.class;
   }

   @Deprecated
   public Holder$Reference<EntityType<?>> builtInRegistryHolder() {
      return this.builtInRegistryHolder;
   }

   public boolean isAllowedInPeaceful() {
      return this.allowedInPeaceful;
   }

   public boolean onlyOpCanSetNbt() {
      return EntityTypes.OP_ONLY_CUSTOM_DATA.contains(this);
   }
}
