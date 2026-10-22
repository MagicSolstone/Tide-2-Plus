package net.minecraft.world.entity;

enum ConversionType$2 {
   private ConversionType$2(final boolean discardAfterConversion) {
   }

   @Override
   public void convert(final Mob from, final Mob to, final ConversionParams params) {
      Entity rootPassenger = from.getFirstPassenger();
      if (rootPassenger != null) {
         rootPassenger.stopRiding();
      }

      Entity leashHolder = from.getLeashHolder();
      if (leashHolder != null) {
         from.dropLeash();
      }

      ConversionType.convertCommon(from, to, params);
   }
}
