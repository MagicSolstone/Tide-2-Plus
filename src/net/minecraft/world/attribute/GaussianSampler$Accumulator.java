package net.minecraft.world.attribute;

@FunctionalInterface
public interface GaussianSampler$Accumulator<V> {
   void accumulate(double weight, V value);
}
