package net.minecraft.world.level.levelgen.feature.stateproviders;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import org.jspecify.annotations.Nullable;

public class RuleBasedStateProvider$Builder {
   @Nullable
   private final BlockStateProvider fallback;
   private final List<RuleBasedStateProvider$Rule> rules = new ArrayList<>();

   public RuleBasedStateProvider$Builder(@Nullable final BlockStateProvider fallback) {
      this.fallback = fallback;
   }

   public RuleBasedStateProvider$Builder ifTrueThenProvide(final BlockPredicate ifTrue, final BlockStateProvider thenProvide) {
      this.rules.add(new RuleBasedStateProvider$Rule(ifTrue, Holder.direct(thenProvide)));
      return this;
   }

   public RuleBasedStateProvider$Builder ifTrueThenProvide(final BlockPredicate ifTrue, final Block thenProvide) {
      this.rules.add(new RuleBasedStateProvider$Rule(ifTrue, BlockStateProvider.holderOf(thenProvide)));
      return this;
   }

   public RuleBasedStateProvider$Builder ifTrueThenProvide(final BlockPredicate ifTrue, final BlockState thenProvide) {
      this.rules.add(new RuleBasedStateProvider$Rule(ifTrue, BlockStateProvider.holderOf(thenProvide)));
      return this;
   }

   public RuleBasedStateProvider build() {
      return new RuleBasedStateProvider(this.fallback == null ? null : Holder.direct(this.fallback), this.rules);
   }
}
