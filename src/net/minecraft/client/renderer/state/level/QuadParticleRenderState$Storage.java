package net.minecraft.client.renderer.state.level;

import java.util.Arrays;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class QuadParticleRenderState$Storage {
   private int capacity = 1024;
   private float[] floatValues = new float[12288];
   private int[] intValues = new int[2048];
   private int currentParticleIndex;

   private QuadParticleRenderState$Storage() {
   }

   public void add(
      final float x,
      final float y,
      final float z,
      final float xRot,
      final float yRot,
      final float zRot,
      final float wRot,
      final float scale,
      final float u0,
      final float u1,
      final float v0,
      final float v1,
      final int color,
      final int lightCoords
   ) {
      if (this.currentParticleIndex >= this.capacity) {
         this.grow();
      }

      int index = this.currentParticleIndex * 12;
      this.floatValues[index++] = x;
      this.floatValues[index++] = y;
      this.floatValues[index++] = z;
      this.floatValues[index++] = xRot;
      this.floatValues[index++] = yRot;
      this.floatValues[index++] = zRot;
      this.floatValues[index++] = wRot;
      this.floatValues[index++] = scale;
      this.floatValues[index++] = u0;
      this.floatValues[index++] = u1;
      this.floatValues[index++] = v0;
      this.floatValues[index] = v1;
      index = this.currentParticleIndex * 2;
      this.intValues[index++] = color;
      this.intValues[index] = lightCoords;
      this.currentParticleIndex++;
   }

   public void forEachParticle(final QuadParticleRenderState$ParticleConsumer consumer) {
      for (int particleIndex = 0; particleIndex < this.currentParticleIndex; particleIndex++) {
         int floatIndex = particleIndex * 12;
         int intIndex = particleIndex * 2;
         consumer.consume(
            this.floatValues[floatIndex++],
            this.floatValues[floatIndex++],
            this.floatValues[floatIndex++],
            this.floatValues[floatIndex++],
            this.floatValues[floatIndex++],
            this.floatValues[floatIndex++],
            this.floatValues[floatIndex++],
            this.floatValues[floatIndex++],
            this.floatValues[floatIndex++],
            this.floatValues[floatIndex++],
            this.floatValues[floatIndex++],
            this.floatValues[floatIndex],
            this.intValues[intIndex++],
            this.intValues[intIndex]
         );
      }
   }

   public void clear() {
      this.currentParticleIndex = 0;
   }

   private void grow() {
      this.capacity *= 2;
      this.floatValues = Arrays.copyOf(this.floatValues, this.capacity * 12);
      this.intValues = Arrays.copyOf(this.intValues, this.capacity * 2);
   }

   public int count() {
      return this.currentParticleIndex;
   }
}
