package net.minecraft.world.level.storage.loot.functions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;

record ApplyBonusCount$OreDrops() implements ApplyBonusCount$Formula {
   public static final ApplyBonusCount$OreDrops INSTANCE = new ApplyBonusCount$OreDrops();
   public static final Codec<ApplyBonusCount$OreDrops> CODEC = MapCodec.unitCodec(INSTANCE);
   public static final ApplyBonusCount$FormulaType TYPE = new ApplyBonusCount$FormulaType(Identifier.withDefaultNamespace("ore_drops"), CODEC);

   @Override
   public int calculateNewCount(final RandomSource random, final int count, final int level) {
      if (level > 0) {
         int bonus = random.nextInt(level + 2) - 1;
         if (bonus < 0) {
            bonus = 0;
         }

         return count * (bonus + 1);
      } else {
         return count;
      }
   }

   @Override
   public ApplyBonusCount$FormulaType getType() {
      return TYPE;
   }
}
