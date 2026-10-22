package net.minecraft.data;

@FunctionalInterface
public interface DataProvider$Factory<T extends DataProvider> {
   T create(PackOutput output);
}
