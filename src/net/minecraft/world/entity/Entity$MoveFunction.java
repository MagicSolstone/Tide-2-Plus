package net.minecraft.world.entity;

@FunctionalInterface
public interface Entity$MoveFunction {
   void accept(Entity target, double x, double y, double z);
}
