package net.minecraft.client.gui.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum ChatComponent$ChatMethod {
   MESSAGE(""),
   COMMAND("/");

   private final String prefix;

   private ChatComponent$ChatMethod(final String prefix) {
      this.prefix = prefix;
   }

   public String prefix() {
      return this.prefix;
   }

   public abstract boolean isDraftRestorable(ChatComponent$Draft draft);
}
