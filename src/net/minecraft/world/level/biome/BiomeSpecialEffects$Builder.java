package net.minecraft.world.level.biome;

import java.util.Optional;
import java.util.OptionalInt;

public class BiomeSpecialEffects$Builder {
   private OptionalInt waterColor = OptionalInt.empty();
   private Optional<Integer> foliageColorOverride = Optional.empty();
   private Optional<Integer> dryFoliageColorOverride = Optional.empty();
   private Optional<Integer> grassColorOverride = Optional.empty();
   private BiomeSpecialEffects$GrassColorModifier grassColorModifier = BiomeSpecialEffects$GrassColorModifier.NONE;

   public BiomeSpecialEffects$Builder waterColor(final int waterColor) {
      this.waterColor = OptionalInt.of(waterColor);
      return this;
   }

   public BiomeSpecialEffects$Builder foliageColorOverride(final int foliageColor) {
      this.foliageColorOverride = Optional.of(foliageColor);
      return this;
   }

   public BiomeSpecialEffects$Builder dryFoliageColorOverride(final int dryFoliageColor) {
      this.dryFoliageColorOverride = Optional.of(dryFoliageColor);
      return this;
   }

   public BiomeSpecialEffects$Builder grassColorOverride(final int grassColor) {
      this.grassColorOverride = Optional.of(grassColor);
      return this;
   }

   public BiomeSpecialEffects$Builder grassColorModifier(final BiomeSpecialEffects$GrassColorModifier grassModifier) {
      this.grassColorModifier = grassModifier;
      return this;
   }

   public BiomeSpecialEffects build() {
      return new BiomeSpecialEffects(
         this.waterColor.orElseThrow(() -> new IllegalStateException("Missing 'water' color.")),
         this.foliageColorOverride,
         this.dryFoliageColorOverride,
         this.grassColorOverride,
         this.grassColorModifier
      );
   }
}
