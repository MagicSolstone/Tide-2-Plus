package net.minecraft.world.level.levelgen.material.rule;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;

public enum OreVeinRule$VeinType {
   COPPER(Blocks.COPPER_ORE.defaultBlockState(), Blocks.RAW_COPPER_BLOCK.defaultBlockState(), Blocks.GRANITE.defaultBlockState(), 0, 50),
   IRON(Blocks.DEEPSLATE_IRON_ORE.defaultBlockState(), Blocks.RAW_IRON_BLOCK.defaultBlockState(), Blocks.TUFF.defaultBlockState(), -60, -8);

   private final BlockState oreBlock;
   private final BlockState rawOreBlock;
   private final BlockState fillerBlock;
   public final int minY;
   public final int maxY;

   private OreVeinRule$VeinType(final BlockState oreBlock, final BlockState rawOreBlock, final BlockState fillerBlock, final int minY, final int maxY) {
      this.oreBlock = oreBlock;
      this.rawOreBlock = rawOreBlock;
      this.fillerBlock = fillerBlock;
      this.minY = minY;
      this.maxY = maxY;
   }

   public OreVeinRule create(final DensityFunction density, final DensityFunction richness, final DensityFunction fillerGap) {
      return new OreVeinRule(this.oreBlock, this.rawOreBlock, this.fillerBlock, 0.02F, density, richness, fillerGap);
   }
}
