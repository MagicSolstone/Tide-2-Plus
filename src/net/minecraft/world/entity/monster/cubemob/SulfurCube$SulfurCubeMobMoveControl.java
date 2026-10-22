package net.minecraft.world.entity.monster.cubemob;

public class SulfurCube$SulfurCubeMobMoveControl<T extends SulfurCube> extends AbstractCubeMob$CubeMobMoveControl<T> {
   public SulfurCube$SulfurCubeMobMoveControl(final T cubeMob) {
      super(cubeMob);
   }

   @Override
   public void tick() {
      if (!this.mob.hasBodyItem()) {
         super.tick();
      }
   }
}
