package net.minecraft.world.level.levelgen.feature;

import java.util.List;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.feature.featuresize.FeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;

public class TreeFeature$Builder {
   public final Holder<BlockStateProvider> trunkProvider;
   private final TrunkPlacer trunkPlacer;
   public final Holder<BlockStateProvider> foliageProvider;
   private final FoliagePlacer foliagePlacer;
   private final Optional<RootPlacer> rootPlacer;
   private final FeatureSize minimumSize;
   private List<TreeDecorator> decorators = List.of();
   private boolean ignoreVines;
   private Holder<BlockStateProvider> belowTrunkProvider;

   public TreeFeature$Builder(
      final Holder<BlockStateProvider> trunkProvider,
      final TrunkPlacer trunkPlacer,
      final Holder<BlockStateProvider> foliageProvider,
      final FoliagePlacer foliagePlacer,
      final Optional<RootPlacer> rootPlacer,
      final FeatureSize minimumSize,
      final Holder<BlockStateProvider> belowTrunkProvider
   ) {
      this.trunkProvider = trunkProvider;
      this.trunkPlacer = trunkPlacer;
      this.foliageProvider = foliageProvider;
      this.foliagePlacer = foliagePlacer;
      this.rootPlacer = rootPlacer;
      this.minimumSize = minimumSize;
      this.belowTrunkProvider = belowTrunkProvider;
   }

   public TreeFeature$Builder(
      final BlockStateProvider trunkProvider,
      final TrunkPlacer trunkPlacer,
      final BlockStateProvider foliageProvider,
      final FoliagePlacer foliagePlacer,
      final Optional<RootPlacer> rootPlacer,
      final FeatureSize minimumSize,
      final Holder<BlockStateProvider> belowTrunkProvider
   ) {
      this(Holder.direct(trunkProvider), trunkPlacer, Holder.direct(foliageProvider), foliagePlacer, rootPlacer, minimumSize, belowTrunkProvider);
   }

   public TreeFeature$Builder(
      final BlockStateProvider trunkProvider,
      final TrunkPlacer trunkPlacer,
      final BlockStateProvider foliageProvider,
      final FoliagePlacer foliagePlacer,
      final FeatureSize minimumSize,
      final Holder<BlockStateProvider> belowTrunkProvider
   ) {
      this(trunkProvider, trunkPlacer, foliageProvider, foliagePlacer, Optional.empty(), minimumSize, belowTrunkProvider);
   }

   public TreeFeature$Builder belowTrunkProvider(final Holder<BlockStateProvider> belowTrunkProvider) {
      this.belowTrunkProvider = belowTrunkProvider;
      return this;
   }

   public TreeFeature$Builder decorators(final List<TreeDecorator> decorators) {
      this.decorators = decorators;
      return this;
   }

   public TreeFeature$Builder ignoreVines() {
      this.ignoreVines = true;
      return this;
   }

   public TreeFeature build() {
      return new TreeFeature(
         this.trunkProvider,
         this.trunkPlacer,
         this.foliageProvider,
         this.foliagePlacer,
         this.rootPlacer,
         this.minimumSize,
         this.decorators,
         this.ignoreVines,
         this.belowTrunkProvider
      );
   }
}
