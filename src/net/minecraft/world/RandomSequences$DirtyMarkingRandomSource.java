package net.minecraft.world;

import java.util.Objects;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;

class RandomSequences$DirtyMarkingRandomSource implements RandomSource {
   private final RandomSource random;

   private RandomSequences$DirtyMarkingRandomSource(final RandomSequences this$0, final RandomSource random) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.random = random;
   }

   @Override
   public RandomSource fork() {
      this.this$0.setDirty();
      return this.random.fork();
   }

   @Override
   public PositionalRandomFactory forkPositional() {
      this.this$0.setDirty();
      return this.random.forkPositional();
   }

   @Override
   public void setSeed(final long seed) {
      this.this$0.setDirty();
      this.random.setSeed(seed);
   }

   @Override
   public int nextInt() {
      this.this$0.setDirty();
      return this.random.nextInt();
   }

   @Override
   public int nextInt(final int bound) {
      this.this$0.setDirty();
      return this.random.nextInt(bound);
   }

   @Override
   public long nextLong() {
      this.this$0.setDirty();
      return this.random.nextLong();
   }

   @Override
   public boolean nextBoolean() {
      this.this$0.setDirty();
      return this.random.nextBoolean();
   }

   @Override
   public float nextFloat() {
      this.this$0.setDirty();
      return this.random.nextFloat();
   }

   @Override
   public double nextDouble() {
      this.this$0.setDirty();
      return this.random.nextDouble();
   }

   @Override
   public double nextGaussian() {
      this.this$0.setDirty();
      return this.random.nextGaussian();
   }

   @Override
   public boolean equals(final Object obj) {
      if (this == obj) {
         return true;
      } else {
         return obj instanceof RandomSequences$DirtyMarkingRandomSource other ? this.random.equals(other.random) : false;
      }
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.random);
   }
}
