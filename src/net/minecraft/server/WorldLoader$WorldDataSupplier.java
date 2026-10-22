package net.minecraft.server;

@FunctionalInterface
public interface WorldLoader$WorldDataSupplier<D> {
   WorldLoader$DataLoadOutput<D> get(WorldLoader$DataLoadContext context);
}
