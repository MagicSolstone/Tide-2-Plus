package net.minecraft.world.level.storage.loot.functions;

import net.minecraft.util.RandomSource;

interface ApplyBonusCount$Formula {
   int calculateNewCount(final RandomSource random, final int count, final int level);

   ApplyBonusCount$FormulaType getType();
}
