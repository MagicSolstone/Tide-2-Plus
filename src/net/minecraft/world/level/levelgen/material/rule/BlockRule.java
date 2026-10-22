package net.minecraft.world.level.levelgen.material.rule;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.material.MaterialRuleContext;

public record BlockRule(BlockState resultState) implements MaterialRule, RuleEvaluator {
   public static final MapCodec<BlockRule> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(BlockState.CODEC.fieldOf("result_state").forGetter(BlockRule::resultState)).apply(i, BlockRule::new)
   );

   @Override
   public MapCodec<BlockRule> codec() {
      return CODEC;
   }

   @Override
   public RuleEvaluator compile(final MaterialRuleContext context) {
      return this;
   }

   @Override
   public BlockState tryApply(final int blockX, final int blockY, final int blockZ) {
      return this.resultState;
   }
}
