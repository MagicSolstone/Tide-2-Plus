package net.minecraft.world.item;

import java.util.Optional;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.LevelEvent$Value;
import net.minecraft.world.phys.Vec3;

public class ProjectileItem$DispenseConfig$Builder {
   private ProjectileItem$PositionFunction positionFunction = (source, direction) -> DispenserBlock.getDispensePosition(source, 0.7, new Vec3(0.0, 0.1, 0.0));
   private float uncertainty = 6.0F;
   private float power = 1.1F;
   private Optional<Integer> overrideDispenseEvent = Optional.empty();

   public ProjectileItem$DispenseConfig$Builder positionFunction(final ProjectileItem$PositionFunction positionFunction) {
      this.positionFunction = positionFunction;
      return this;
   }

   public ProjectileItem$DispenseConfig$Builder uncertainty(final float uncertainty) {
      this.uncertainty = uncertainty;
      return this;
   }

   public ProjectileItem$DispenseConfig$Builder power(final float power) {
      this.power = power;
      return this;
   }

   public ProjectileItem$DispenseConfig$Builder overrideDispenseEvent(@LevelEvent$Value final int dispenseEvent) {
      this.overrideDispenseEvent = Optional.of(dispenseEvent);
      return this;
   }

   public ProjectileItem$DispenseConfig build() {
      return new ProjectileItem$DispenseConfig(this.positionFunction, this.uncertainty, this.power, this.overrideDispenseEvent);
   }
}
