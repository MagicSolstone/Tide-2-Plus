package net.minecraft.world.level.levelgen.synth;

public record GradientNoise$Gradient(int x, int y, int z) {
   public double dot(final double x, final double y, final double z) {
      return this.x * x + this.y * y + this.z * z;
   }

   public float dot(final float x, final float y, final float z) {
      return this.x * x + this.y * y + this.z * z;
   }

   public float dotXz(final float x, final float z) {
      return this.x * x + this.z * z;
   }
}
