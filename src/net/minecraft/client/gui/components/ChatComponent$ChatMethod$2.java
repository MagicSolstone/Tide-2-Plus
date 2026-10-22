package net.minecraft.client.gui.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
enum ChatComponent$ChatMethod$2 {
   private ChatComponent$ChatMethod$2(final String prefix) {
   }

   @Override
   public boolean isDraftRestorable(final ChatComponent$Draft draft) {
      return this == draft.chatMethod;
   }
}
