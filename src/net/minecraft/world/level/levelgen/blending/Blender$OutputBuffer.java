package net.minecraft.world.level.levelgen.blending;

import net.minecraft.core.QuartPos;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;

public record Blender$OutputBuffer(Blender blender, int minCellX, int minCellZ, int cellCountX, int cellCountZ, float[] alphas, float[] offsets) {
   private static final int NO_VALUE = -1;

   public DensitySampler createAlphaSampler() {
      return new Blender$OutputBuffer$1(this);
   }

   public DensitySampler createOffsetSampler() {
      return new Blender$OutputBuffer$2(this);
   }

   public float getAlpha(final int blockX, final int blockZ) {
      int index = this.getIndex(blockX, blockZ);
      return index == -1 ? this.blender.blendOffsetAndFactor(blockX, blockZ).alpha() : this.alphas[index];
   }

   public float getOffset(final int blockX, final int blockZ) {
      int index = this.getIndex(blockX, blockZ);
      return index == -1 ? this.blender.blendOffsetAndFactor(blockX, blockZ).blendingOffset() : this.offsets[index];
   }

   private int getIndex(final int blockX, final int blockZ) {
      int relativeX = QuartPos.fromBlock(blockX) - this.minCellX;
      int relativeZ = QuartPos.fromBlock(blockZ) - this.minCellZ;
      return relativeX >= 0 && relativeZ >= 0 && relativeX < this.cellCountX && relativeZ < this.cellCountZ ? relativeX + relativeZ * this.cellCountX : -1;
   }
}
