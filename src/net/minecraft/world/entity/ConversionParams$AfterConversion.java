package net.minecraft.world.entity;

@FunctionalInterface
public interface ConversionParams$AfterConversion<T extends Mob> {
   void finalizeConversion(T mob);
}
