package net.minecraft.world.entity.animal.sheep;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;

@FunctionalInterface
interface SheepColorSpawnRules$SheepColorProvider {
   DyeColor get(RandomSource random);
}
