package net.minecraft.world.entity;

import java.util.Optional;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.DependantName;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Util;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.Vec3;

public class EntityType$Builder<T extends Entity> {
   private final EntityType$EntityFactory<T> factory;
   private final MobCategory category;
   private TagKey<Block> immuneTo = BlockTags.DEFAULT_IMMUNE_TO;
   private boolean serialize = true;
   private boolean summon = true;
   private boolean fireImmune;
   private boolean canSpawnFarFromPlayer;
   private int clientTrackingRange = 5;
   private int updateInterval = 3;
   private EntityDimensions dimensions = EntityDimensions.scalable(0.6F, 1.8F);
   private float spawnDimensionsScale = 1.0F;
   private EntityAttachments$Builder attachments = EntityAttachments.builder();
   private FeatureFlagSet requiredFeatures = FeatureFlags.VANILLA_SET;
   private DependantName<EntityType<?>, Optional<ResourceKey<LootTable>>> lootTable = id -> Optional.of(
      ResourceKey.create(Registries.LOOT_TABLE, id.identifier().withPrefix("entities/"))
   );
   private final DependantName<EntityType<?>, String> descriptionId = id -> Util.makeDescriptionId("entity", id.identifier());
   private boolean allowedInPeaceful = true;
   private boolean trackDeltas = true;

   private EntityType$Builder(final EntityType$EntityFactory<T> factory, final MobCategory category) {
      this.factory = factory;
      this.category = category;
      this.canSpawnFarFromPlayer = category == MobCategory.CREATURE || category == MobCategory.MISC;
   }

   public static <T extends Entity> EntityType$Builder<T> of(final EntityType$EntityFactory<T> factory, final MobCategory category) {
      return new EntityType$Builder<>(factory, category);
   }

   public static <T extends Entity> EntityType$Builder<T> createNothing(final MobCategory category) {
      return new EntityType$Builder<>((t, l) -> null, category);
   }

   public EntityType$Builder<T> sized(final float width, final float height) {
      this.dimensions = EntityDimensions.scalable(width, height);
      return this;
   }

   public EntityType$Builder<T> spawnDimensionsScale(final float scale) {
      this.spawnDimensionsScale = scale;
      return this;
   }

   public EntityType$Builder<T> eyeHeight(final float eyeHeight) {
      this.dimensions = this.dimensions.withEyeHeight(eyeHeight);
      return this;
   }

   public EntityType$Builder<T> passengerAttachments(final float... offsetYs) {
      for (float offsetY : offsetYs) {
         this.attachments = this.attachments.attach(EntityAttachment.PASSENGER, 0.0F, offsetY, 0.0F);
      }

      return this;
   }

   public EntityType$Builder<T> passengerAttachments(final Vec3... points) {
      for (Vec3 point : points) {
         this.attachments = this.attachments.attach(EntityAttachment.PASSENGER, point);
      }

      return this;
   }

   public EntityType$Builder<T> vehicleAttachment(final Vec3 point) {
      return this.attach(EntityAttachment.VEHICLE, point);
   }

   public EntityType$Builder<T> ridingOffset(final float ridingOffset) {
      return this.attach(EntityAttachment.VEHICLE, 0.0F, -ridingOffset, 0.0F);
   }

   public EntityType$Builder<T> nameTagOffset(final float nameTagOffset) {
      return this.attach(EntityAttachment.NAME_TAG, 0.0F, nameTagOffset, 0.0F);
   }

   public EntityType$Builder<T> attach(final EntityAttachment attachment, final float x, final float y, final float z) {
      this.attachments = this.attachments.attach(attachment, x, y, z);
      return this;
   }

   public EntityType$Builder<T> attach(final EntityAttachment attachment, final Vec3 point) {
      this.attachments = this.attachments.attach(attachment, point);
      return this;
   }

   public EntityType$Builder<T> noSummon() {
      this.summon = false;
      return this;
   }

   public EntityType$Builder<T> noSave() {
      this.serialize = false;
      return this;
   }

   public EntityType$Builder<T> fireImmune() {
      this.fireImmune = true;
      return this;
   }

   public EntityType$Builder<T> immuneTo(final TagKey<Block> tag) {
      this.immuneTo = tag;
      return this;
   }

   public EntityType$Builder<T> canSpawnFarFromPlayer() {
      this.canSpawnFarFromPlayer = true;
      return this;
   }

   public EntityType$Builder<T> clientTrackingRange(final int clientChunkRange) {
      this.clientTrackingRange = clientChunkRange;
      return this;
   }

   public EntityType$Builder<T> updateInterval(final int updateInterval) {
      this.updateInterval = updateInterval;
      return this;
   }

   public EntityType$Builder<T> noUpdateInterval() {
      return this.updateInterval(Integer.MAX_VALUE);
   }

   public EntityType$Builder<T> requiredFeatures(final FeatureFlag... flags) {
      this.requiredFeatures = FeatureFlags.REGISTRY.subset(flags);
      return this;
   }

   public EntityType$Builder<T> noLootTable() {
      this.lootTable = DependantName.fixed(Optional.empty());
      return this;
   }

   public EntityType$Builder<T> notInPeaceful() {
      this.allowedInPeaceful = false;
      return this;
   }

   public EntityType$Builder<T> dontTrackDeltas() {
      this.trackDeltas = false;
      return this;
   }

   public EntityType<T> build(final ResourceKey<EntityType<?>> name) {
      if (this.serialize) {
         Util.fetchChoiceType(References.ENTITY_TREE, name.identifier().toString());
      }

      return new EntityType<>(
         this.factory,
         this.category,
         this.serialize,
         this.summon,
         this.fireImmune,
         this.canSpawnFarFromPlayer,
         this.immuneTo,
         this.dimensions.withAttachments(this.attachments),
         this.spawnDimensionsScale,
         this.clientTrackingRange,
         this.updateInterval,
         this.descriptionId.get(name),
         this.lootTable.get(name),
         this.requiredFeatures,
         this.allowedInPeaceful,
         this.trackDeltas
      );
   }
}
