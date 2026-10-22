package net.minecraft.world.entity;

import net.minecraft.world.item.ItemStack;

enum ConversionType$1 {
   private ConversionType$1(final boolean discardAfterConversion) {
   }

   @Override
   public void convert(final Mob from, final Mob to, final ConversionParams params) {
      Entity rootPassenger = from.getFirstPassenger();
      to.copyPosition(from);
      to.setDeltaMovement(from.getDeltaMovement());
      if (rootPassenger != null) {
         rootPassenger.stopRiding();
         rootPassenger.boardingCooldown = 0;

         for (Entity passenger : to.getPassengers()) {
            passenger.stopRiding();
            passenger.remove(Entity$RemovalReason.DISCARDED);
         }

         rootPassenger.startRiding(to);
      }

      Entity vehicle = from.getVehicle();
      if (vehicle != null) {
         from.stopRiding();
         to.startRiding(vehicle, false, false);
      }

      if (params.keepEquipment()) {
         for (EquipmentSlot slot : EquipmentSlot.VALUES) {
            ItemStack itemStack = from.getItemBySlot(slot);
            if (!itemStack.isEmpty()) {
               to.setItemSlot(slot, itemStack.copyAndClear());
               to.setDropChance(slot, from.getDropChances().byEquipment(slot));
            }
         }
      }

      to.fallDistance = from.fallDistance;
      to.setSharedFlag(7, from.isFallFlying());
      to.lastHurtByPlayerMemoryTime = from.lastHurtByPlayerMemoryTime;
      to.hurtTime = from.hurtTime;
      to.yBodyRot = from.yBodyRot;
      to.setOnGround(from.onGround());
      from.getSleepingPos().ifPresent(to::setSleepingPos);
      Entity leashHolder = from.getLeashHolder();
      if (leashHolder != null) {
         to.setLeashedTo(leashHolder, true);
      }

      ConversionType.convertCommon(from, to, params);
   }
}
