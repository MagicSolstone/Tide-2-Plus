package net.minecraft.commands;

import net.minecraft.network.chat.Component;

class CommandSource$1 implements CommandSource {
   @Override
   public void sendSystemMessage(final Component message) {
   }

   @Override
   public boolean acceptsSuccess() {
      return false;
   }

   @Override
   public boolean acceptsFailure() {
      return false;
   }

   @Override
   public boolean shouldInformAdmins() {
      return false;
   }
}
