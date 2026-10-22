package net.minecraft.world.attribute;

@FunctionalInterface
public interface GaussianSampler$Sampler<V> {
   V get(int x, int y, int z);
}
