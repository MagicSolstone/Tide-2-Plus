package net.minecraft.world.level.levelgen.feature;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;

public class FallenTreeFeature$Builder {
   private final BlockStateProvider trunkProvider;
   private final IntProvider logLength;
   private final List<TreeDecorator> stumpDecorators = new ArrayList<>();
   private final List<TreeDecorator> logDecorators = new ArrayList<>();

   public FallenTreeFeature$Builder(final BlockStateProvider trunkProvider, final IntProvider logLength) {
      this.trunkProvider = trunkProvider;
      this.logLength = logLength;
   }

   public FallenTreeFeature$Builder stumpDecorator(final TreeDecorator stumpDecorator) {
      this.stumpDecorators.add(stumpDecorator);
      return this;
   }

   public FallenTreeFeature$Builder logDecorator(final TreeDecorator logDecorator) {
      this.logDecorators.add(logDecorator);
      return this;
   }

   public FallenTreeFeature build() {
      return new FallenTreeFeature(Holder.direct(this.trunkProvider), this.logLength, List.copyOf(this.stumpDecorators), List.copyOf(this.logDecorators));
   }
}
