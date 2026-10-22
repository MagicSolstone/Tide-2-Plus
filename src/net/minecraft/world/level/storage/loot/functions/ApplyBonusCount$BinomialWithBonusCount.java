package net.minecraft.world.level.storage.loot.functions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;

record ApplyBonusCount$BinomialWithBonusCount(int extraRounds, float probability) implements ApplyBonusCount$Formula {
   private static final Codec<ApplyBonusCount$BinomialWithBonusCount> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Codec.INT.fieldOf("extra").forGetter(ApplyBonusCount$BinomialWithBonusCount::extraRounds),
            Codec.FLOAT.fieldOf("probability").forGetter(ApplyBonusCount$BinomialWithBonusCount::probability)
         )
         .apply(i, ApplyBonusCount$BinomialWithBonusCount::new)
   );
   public static final ApplyBonusCount$FormulaType TYPE = new ApplyBonusCount$FormulaType(Identifier.withDefaultNamespace("binomial_with_bonus_count"), CODEC);

   @Override
   public int calculateNewCount(final RandomSource random, int count, final int level) {
      for (int i = 0; i < level + this.extraRounds; i++) {
         if (random.nextFloat() < this.probability) {
            count++;
         }
      }

      return count;
   }

   @Override
   public ApplyBonusCount$FormulaType getType() {
      return TYPE;
   }
}
