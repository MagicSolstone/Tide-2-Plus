package net.minecraft.world.entity.monster.cubemob;

import java.util.EnumSet;
import java.util.Objects;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal$Flag;
import net.minecraft.world.entity.item.ItemEntity;
import org.jspecify.annotations.Nullable;

class SulfurCube$SulfurCubeSearchForItemsGoal extends Goal {
   private final SulfurCube sulfurCube;
   @Nullable
   private ItemEntity targetItem;

   public SulfurCube$SulfurCubeSearchForItemsGoal(final SulfurCube this$0, final SulfurCube sulfurCube) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.setFlags(EnumSet.of(Goal$Flag.LOOK));
      this.sulfurCube = sulfurCube;
   }

   @Override
   public boolean canUse() {
      if (!this.sulfurCube.isBaby() && this.sulfurCube.pickupTimer <= 0) {
         this.targetItem = getServerLevel(this.sulfurCube)
            .getNearestEntity(
               this.sulfurCube.level().getEntitiesOfClass(ItemEntity.class, this.sulfurCube.getBoundingBox().inflate(8.0, 8.0, 8.0), SulfurCube.ALLOWED_ITEMS),
               this.sulfurCube.getX(),
               this.sulfurCube.getY(),
               this.sulfurCube.getZ()
            );
         return this.targetItem != null;
      } else {
         return false;
      }
   }

   @Override
   public void tick() {
      this.this$0.lookAt(this.targetItem, 10.0F, 10.0F);
      if (this.this$0.getMoveControl() instanceof AbstractCubeMob$CubeMobMoveControl cubeMobMoveControl) {
         cubeMobMoveControl.setDirection(this.this$0.getYRot(), true);
      }
   }
}
