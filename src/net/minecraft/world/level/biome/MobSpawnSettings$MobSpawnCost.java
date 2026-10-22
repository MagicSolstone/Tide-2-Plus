package net.minecraft.world.level.biome;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record MobSpawnSettings$MobSpawnCost(double energyBudget, double charge) {
   public static final Codec<MobSpawnSettings$MobSpawnCost> CODEC = RecordCodecBuilder.create(
      i -> i.group(Codec.DOUBLE.fieldOf("energy_budget").forGetter(e -> e.energyBudget), Codec.DOUBLE.fieldOf("charge").forGetter(e -> e.charge))
         .apply(i, MobSpawnSettings$MobSpawnCost::new)
   );
}
