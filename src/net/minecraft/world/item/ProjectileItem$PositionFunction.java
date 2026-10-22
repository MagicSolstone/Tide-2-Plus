package net.minecraft.world.item;

import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.BlockSource;

@FunctionalInterface
public interface ProjectileItem$PositionFunction {
   Position getDispensePosition(final BlockSource source, final Direction direction);
}
