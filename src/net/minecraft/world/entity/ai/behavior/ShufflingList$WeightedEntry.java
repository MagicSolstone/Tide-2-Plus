package net.minecraft.world.entity.ai.behavior;

import com.mojang.serialization.Codec;

public class ShufflingList$WeightedEntry<T> {
   private final T data;
   private final int weight;
   private double randWeight;

   private ShufflingList$WeightedEntry(final T data, final int weight) {
      this.weight = weight;
      this.data = data;
   }

   private double getRandWeight() {
      return this.randWeight;
   }

   private void setRandom(final float random) {
      this.randWeight = -Math.pow(random, 1.0F / this.weight);
   }

   public T getData() {
      return this.data;
   }

   public int getWeight() {
      return this.weight;
   }

   @Override
   public String toString() {
      return this.weight + ":" + this.data;
   }

   public static <E> Codec<ShufflingList$WeightedEntry<E>> codec(final Codec<E> elementCodec) {
      return new ShufflingList$WeightedEntry$1(elementCodec);
   }
}
