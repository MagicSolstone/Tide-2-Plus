package net.minecraft.world.level.storage.loot.functions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;

record ApplyBonusCount$UniformBonusCount(int bonusMultiplier) implements ApplyBonusCount$Formula {
   public static final Codec<ApplyBonusCount$UniformBonusCount> CODEC = RecordCodecBuilder.create(
      i -> i.group(Codec.INT.fieldOf("bonusMultiplier").forGetter(ApplyBonusCount$UniformBonusCount::bonusMultiplier))
         .apply(i, ApplyBonusCount$UniformBonusCount::new)
   );
   public static final ApplyBonusCount$FormulaType TYPE = new ApplyBonusCount$FormulaType(Identifier.withDefaultNamespace("uniform_bonus_count"), CODEC);

   @Override
   public int calculateNewCount(final RandomSource random, final int count, final int level) {
      return count + random.nextInt(this.bonusMultiplier * level + 1);
   }

   @Override
   public ApplyBonusCount$FormulaType getType() {
      return TYPE;
   }
}
