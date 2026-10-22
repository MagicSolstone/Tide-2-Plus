package net.minecraft.world.item;

@FunctionalInterface
public interface CreativeModeTab$DisplayItemsGenerator {
   void accept(CreativeModeTab$ItemDisplayParameters parameters, CreativeModeTab$Output output);
}
