package net.minecraft.core.component;

import java.util.List;
import java.util.Optional;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.storage.loot.LootTable;

public class BlockTransformer$BlockTransformData$Builder {
   private final Holder<BlockStateProvider> targetStateProvider;
   private Holder<SoundEvent> sound = BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.EMPTY);
   private BlockTransformer$TransformParticle particle = BlockTransformer$TransformParticle.NONE;
   private List<Direction> disallowedFaces = List.of();
   private Optional<ResourceKey<LootTable>> loot = Optional.empty();
   private BlockTransformer$DropStrategy dropStrategy = BlockTransformer$DropStrategy.FROM_MIDDLE;
   private boolean updateFromNeighbors = true;
   private BlockTransformer$TransformType transformType = BlockTransformer$TransformType.SINGLE_BLOCK;
   private boolean consumeOnUse = true;
   private int itemDamagePerUse = 1;

   private BlockTransformer$BlockTransformData$Builder(final Holder<BlockStateProvider> targetStateProvider) {
      this.targetStateProvider = targetStateProvider;
   }

   public BlockTransformer$BlockTransformData$Builder sound(final Holder<SoundEvent> sound) {
      this.sound = sound;
      return this;
   }

   public BlockTransformer$BlockTransformData$Builder particle(final BlockTransformer$TransformParticle particle) {
      this.particle = particle;
      return this;
   }

   public BlockTransformer$BlockTransformData$Builder disallowedFaces(final List<Direction> disallowedFaces) {
      this.disallowedFaces = disallowedFaces;
      return this;
   }

   public BlockTransformer$BlockTransformData$Builder loot(final ResourceKey<LootTable> loot) {
      this.loot = Optional.of(loot);
      return this;
   }

   public BlockTransformer$BlockTransformData$Builder dropStrategy(final BlockTransformer$DropStrategy dropStrategy) {
      this.dropStrategy = dropStrategy;
      return this;
   }

   public BlockTransformer$BlockTransformData$Builder updateFromNeighbors(final boolean updateFromNeighbors) {
      this.updateFromNeighbors = updateFromNeighbors;
      return this;
   }

   public BlockTransformer$BlockTransformData$Builder transformType(final BlockTransformer$TransformType transformType) {
      this.transformType = transformType;
      return this;
   }

   public BlockTransformer$BlockTransformData$Builder consumeOnUse(final boolean consumeOnUse) {
      this.consumeOnUse = consumeOnUse;
      return this;
   }

   public BlockTransformer$BlockTransformData$Builder itemDamagePerUse(final int itemDamagePerUse) {
      this.itemDamagePerUse = itemDamagePerUse;
      return this;
   }

   public BlockTransformer$BlockTransformData build() {
      return new BlockTransformer$BlockTransformData(
         this.targetStateProvider,
         this.sound,
         this.particle,
         this.disallowedFaces,
         this.loot,
         this.dropStrategy,
         this.updateFromNeighbors,
         this.transformType,
         this.consumeOnUse,
         this.itemDamagePerUse
      );
   }
}
