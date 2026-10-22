package net.minecraft.world.level.block.entity;

import java.util.Objects;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.inventory.BeaconMenu;
import net.minecraft.world.inventory.ContainerData;

class BeaconBlockEntity$1 implements ContainerData {
   BeaconBlockEntity$1(final BeaconBlockEntity this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public int get(final int dataId) {
      return switch (dataId) {
         case 0 -> this.this$0.levels;
         case 1 -> BeaconMenu.encodeEffect(this.this$0.primaryPower);
         case 2 -> BeaconMenu.encodeEffect(this.this$0.secondaryPower);
         default -> 0;
      };
   }

   @Override
   public void set(final int dataId, final int value) {
      switch (dataId) {
         case 0:
            this.this$0.levels = value;
            break;
         case 1:
            if (!this.this$0.level.isClientSide() && !this.this$0.beamSections.isEmpty()) {
               BeaconBlockEntity.playSound(this.this$0.level, this.this$0.worldPosition, SoundEvents.BEACON_POWER_SELECT);
            }

            this.this$0.primaryPower = BeaconBlockEntity.filterEffect(BeaconMenu.decodeEffect(value));
            break;
         case 2:
            this.this$0.secondaryPower = BeaconBlockEntity.filterEffect(BeaconMenu.decodeEffect(value));
      }
   }

   @Override
   public int getCount() {
      return 3;
   }
}
