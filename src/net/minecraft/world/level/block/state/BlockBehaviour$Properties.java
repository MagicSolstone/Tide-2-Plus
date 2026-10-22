package net.minecraft.world.level.block.state;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.DependantName;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class BlockBehaviour$Properties {
   private Function<BlockState, MapColor> mapColor = state -> MapColor.NONE;
   private boolean hasCollision = true;
   private SoundType soundType = SoundType.STONE;
   private ToIntFunction<BlockState> lightEmission = state -> 0;
   private float explosionResistance;
   private float destroyTime;
   private boolean requiresCorrectToolForDrops;
   private boolean isRandomlyTicking;
   private float friction = 0.6F;
   private float speedFactor = 1.0F;
   private float jumpFactor = 1.0F;
   private float bounceRestitution;
   @Nullable
   private ResourceKey<Block> id;
   private DependantName<Block, Optional<ResourceKey<LootTable>>> drops = id -> Optional.of(
      ResourceKey.create(Registries.LOOT_TABLE, id.identifier().withPrefix("blocks/"))
   );
   private DependantName<Block, String> descriptionId = id -> Util.makeDescriptionId("block", id.identifier());
   private boolean canOcclude = true;
   private boolean isAir;
   private boolean ignitedByLava;
   @Deprecated
   private boolean liquid;
   @Deprecated
   private boolean forceSolidOff;
   private boolean forceSolidOn;
   private PushReaction pushReaction = PushReaction.PUSH_PULL;
   private boolean spawnTerrainParticles = true;
   private NoteBlockInstrument instrument = NoteBlockInstrument.HARP;
   private boolean replaceable;
   private BlockBehaviour$StateArgumentPredicate<EntityType<?>> isValidSpawn = (state, level, pos, entityType) -> state.isFaceSturdy(level, pos, Direction.UP)
      && state.getLightEmission() < 14;
   private BlockBehaviour$StatePredicate isRedstoneConductor = BlockBehaviour$BlockStateBase::isCollisionShapeFullBlock;
   private BlockBehaviour$StatePredicate isSuffocating = (state, level, pos) -> state.is(BlockTags.CAUSES_SUFFOCATION)
      && state.isCollisionShapeFullBlock(level, pos);
   private BlockBehaviour$StateArgumentPredicate<AABB> isViewBlocking = (state, level, pos, var4) -> this.isSuffocating.test(state, level, pos);
   private BlockBehaviour$PostProcess postProcess = (state, level, pos) -> null;
   private Predicate<BlockState> emissiveRendering = var0 -> false;
   private boolean dynamicShape;
   private FeatureFlagSet requiredFeatures = FeatureFlags.VANILLA_SET;
   @Nullable
   private BlockBehaviour$OffsetFunction offsetFunction;
   private float fallDistanceReduction;

   private BlockBehaviour$Properties() {
   }

   public static BlockBehaviour$Properties of() {
      return new BlockBehaviour$Properties();
   }

   public static BlockBehaviour$Properties ofFullCopy(final BlockBehaviour block) {
      BlockBehaviour$Properties copyTo = ofLegacyCopy(block);
      BlockBehaviour$Properties copyFrom = block.properties;
      copyTo.jumpFactor = copyFrom.jumpFactor;
      copyTo.isRedstoneConductor = copyFrom.isRedstoneConductor;
      copyTo.isValidSpawn = copyFrom.isValidSpawn;
      copyTo.postProcess = copyFrom.postProcess;
      copyTo.isSuffocating = copyFrom.isSuffocating;
      copyTo.isViewBlocking = copyFrom.isViewBlocking;
      copyTo.drops = copyFrom.drops;
      copyTo.descriptionId = copyFrom.descriptionId;
      return copyTo;
   }

   @Deprecated
   public static BlockBehaviour$Properties ofLegacyCopy(final BlockBehaviour block) {
      BlockBehaviour$Properties copyTo = new BlockBehaviour$Properties();
      BlockBehaviour$Properties copyFrom = block.properties;
      copyTo.destroyTime = copyFrom.destroyTime;
      copyTo.explosionResistance = copyFrom.explosionResistance;
      copyTo.hasCollision = copyFrom.hasCollision;
      copyTo.isRandomlyTicking = copyFrom.isRandomlyTicking;
      copyTo.lightEmission = copyFrom.lightEmission;
      copyTo.mapColor = copyFrom.mapColor;
      copyTo.soundType = copyFrom.soundType;
      copyTo.friction = copyFrom.friction;
      copyTo.speedFactor = copyFrom.speedFactor;
      copyTo.bounceRestitution = copyFrom.bounceRestitution;
      copyTo.dynamicShape = copyFrom.dynamicShape;
      copyTo.canOcclude = copyFrom.canOcclude;
      copyTo.isAir = copyFrom.isAir;
      copyTo.ignitedByLava = copyFrom.ignitedByLava;
      copyTo.liquid = copyFrom.liquid;
      copyTo.forceSolidOff = copyFrom.forceSolidOff;
      copyTo.forceSolidOn = copyFrom.forceSolidOn;
      copyTo.pushReaction = copyFrom.pushReaction;
      copyTo.requiresCorrectToolForDrops = copyFrom.requiresCorrectToolForDrops;
      copyTo.offsetFunction = copyFrom.offsetFunction;
      copyTo.spawnTerrainParticles = copyFrom.spawnTerrainParticles;
      copyTo.requiredFeatures = copyFrom.requiredFeatures;
      copyTo.emissiveRendering = copyFrom.emissiveRendering;
      copyTo.instrument = copyFrom.instrument;
      copyTo.replaceable = copyFrom.replaceable;
      return copyTo;
   }

   public BlockBehaviour$Properties mapColor(final DyeColor dyeColor) {
      this.mapColor = state -> dyeColor.getMapColor();
      return this;
   }

   public BlockBehaviour$Properties mapColor(final MapColor mapColor) {
      this.mapColor = state -> mapColor;
      return this;
   }

   public BlockBehaviour$Properties mapColor(final Function<BlockState, MapColor> mapColor) {
      this.mapColor = mapColor;
      return this;
   }

   public BlockBehaviour$Properties noCollision() {
      this.hasCollision = false;
      this.canOcclude = false;
      return this;
   }

   public BlockBehaviour$Properties noOcclusion() {
      this.canOcclude = false;
      return this;
   }

   public BlockBehaviour$Properties friction(final float friction) {
      this.friction = friction;
      return this;
   }

   public BlockBehaviour$Properties speedFactor(final float speedFactor) {
      this.speedFactor = speedFactor;
      return this;
   }

   public BlockBehaviour$Properties jumpFactor(final float jumpFactor) {
      this.jumpFactor = jumpFactor;
      return this;
   }

   public BlockBehaviour$Properties bounceRestitution(final float bounceRestitution) {
      this.bounceRestitution = bounceRestitution;
      return this;
   }

   public BlockBehaviour$Properties fallDistanceReduction(final float fallDistanceReduction) {
      this.fallDistanceReduction = fallDistanceReduction;
      return this;
   }

   public BlockBehaviour$Properties sound(final SoundType soundType) {
      this.soundType = soundType;
      return this;
   }

   public BlockBehaviour$Properties lightLevel(final ToIntFunction<BlockState> lightEmission) {
      this.lightEmission = lightEmission;
      return this;
   }

   public BlockBehaviour$Properties strength(final float destroyTime, final float explosionResistance) {
      return this.destroyTime(destroyTime).explosionResistance(explosionResistance);
   }

   public BlockBehaviour$Properties instabreak() {
      return this.strength(0.0F);
   }

   public BlockBehaviour$Properties strength(final float destroyTime) {
      this.strength(destroyTime, destroyTime);
      return this;
   }

   public BlockBehaviour$Properties randomTicks() {
      this.isRandomlyTicking = true;
      return this;
   }

   public BlockBehaviour$Properties dynamicShape() {
      this.dynamicShape = true;
      return this;
   }

   public BlockBehaviour$Properties noLootTable() {
      this.drops = DependantName.fixed(Optional.empty());
      return this;
   }

   public BlockBehaviour$Properties overrideLootTable(final Optional<ResourceKey<LootTable>> table) {
      this.drops = DependantName.fixed(table);
      return this;
   }

   protected Optional<ResourceKey<LootTable>> effectiveDrops() {
      return this.drops.get(Objects.requireNonNull(this.id, "Block id not set"));
   }

   public BlockBehaviour$Properties ignitedByLava() {
      this.ignitedByLava = true;
      return this;
   }

   public BlockBehaviour$Properties liquid() {
      this.liquid = true;
      return this;
   }

   public BlockBehaviour$Properties forceSolidOn() {
      this.forceSolidOn = true;
      return this;
   }

   @Deprecated
   public BlockBehaviour$Properties forceSolidOff() {
      this.forceSolidOff = true;
      return this;
   }

   public BlockBehaviour$Properties pushReaction(final PushReaction pushReaction) {
      this.pushReaction = pushReaction;
      return this;
   }

   public BlockBehaviour$Properties air() {
      this.isAir = true;
      return this;
   }

   public BlockBehaviour$Properties isValidSpawn(final BlockBehaviour$StateArgumentPredicate<EntityType<?>> isValidSpawn) {
      this.isValidSpawn = isValidSpawn;
      return this;
   }

   public BlockBehaviour$Properties isRedstoneConductor(final BlockBehaviour$StatePredicate isRedstoneConductor) {
      this.isRedstoneConductor = isRedstoneConductor;
      return this;
   }

   public BlockBehaviour$Properties isSuffocating(final BlockBehaviour$StatePredicate isSuffocating) {
      this.isSuffocating = isSuffocating;
      return this;
   }

   public BlockBehaviour$Properties isViewBlocking(final BlockBehaviour$StateArgumentPredicate<AABB> isViewBlocking) {
      this.isViewBlocking = isViewBlocking;
      return this;
   }

   public BlockBehaviour$Properties postProcess(final BlockBehaviour$PostProcess postProcess) {
      this.postProcess = postProcess;
      return this;
   }

   public BlockBehaviour$Properties emissiveRendering(final Predicate<BlockState> emissiveRendering) {
      this.emissiveRendering = emissiveRendering;
      return this;
   }

   public BlockBehaviour$Properties requiresCorrectToolForDrops() {
      this.requiresCorrectToolForDrops = true;
      return this;
   }

   public BlockBehaviour$Properties destroyTime(final float destroyTime) {
      this.destroyTime = destroyTime;
      return this;
   }

   public BlockBehaviour$Properties explosionResistance(final float explosionResistance) {
      this.explosionResistance = Math.max(0.0F, explosionResistance);
      return this;
   }

   public BlockBehaviour$Properties offsetType(final BlockBehaviour$OffsetType offsetType) {
      this.offsetFunction = switch (offsetType) {
         case NONE -> null;
         case XZ -> (state, pos) -> {
            Block block = state.getBlock();
            long seed = Mth.getSeed(pos.getX(), 0, pos.getZ());
            float maxHorizontalOffset = block.getMaxHorizontalOffset();
            double x = Mth.clamp(((float)(seed & 15L) / 15.0F - 0.5) * 0.5, (double)(-maxHorizontalOffset), (double)maxHorizontalOffset);
            double z = Mth.clamp(((float)(seed >> 8 & 15L) / 15.0F - 0.5) * 0.5, (double)(-maxHorizontalOffset), (double)maxHorizontalOffset);
            return new Vec3(x, 0.0, z);
         };
         case XYZ -> (state, pos) -> {
            Block block = state.getBlock();
            long seed = Mth.getSeed(pos.getX(), 0, pos.getZ());
            double y = ((float)(seed >> 4 & 15L) / 15.0F - 1.0) * block.getMaxVerticalOffset();
            float maxHorizontalOffset = block.getMaxHorizontalOffset();
            double x = Mth.clamp(((float)(seed & 15L) / 15.0F - 0.5) * 0.5, (double)(-maxHorizontalOffset), (double)maxHorizontalOffset);
            double z = Mth.clamp(((float)(seed >> 8 & 15L) / 15.0F - 0.5) * 0.5, (double)(-maxHorizontalOffset), (double)maxHorizontalOffset);
            return new Vec3(x, y, z);
         };
      };
      return this;
   }

   public BlockBehaviour$Properties noTerrainParticles() {
      this.spawnTerrainParticles = false;
      return this;
   }

   public BlockBehaviour$Properties requiredFeatures(final FeatureFlag... flags) {
      this.requiredFeatures = FeatureFlags.REGISTRY.subset(flags);
      return this;
   }

   public BlockBehaviour$Properties instrument(final NoteBlockInstrument instrument) {
      this.instrument = instrument;
      return this;
   }

   public BlockBehaviour$Properties replaceable() {
      this.replaceable = true;
      return this;
   }

   public BlockBehaviour$Properties setId(final ResourceKey<Block> id) {
      this.id = id;
      return this;
   }

   public BlockBehaviour$Properties overrideDescription(final String descriptionId) {
      this.descriptionId = DependantName.fixed(descriptionId);
      return this;
   }

   protected String effectiveDescriptionId() {
      return this.descriptionId.get(Objects.requireNonNull(this.id, "Block id not set"));
   }
}
