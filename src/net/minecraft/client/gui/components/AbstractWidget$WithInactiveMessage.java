package net.minecraft.client.gui.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.Style;

@Environment(EnvType.CLIENT)
public abstract class AbstractWidget$WithInactiveMessage extends AbstractWidget {
   private Component inactiveMessage;

   public static Component defaultInactiveMessage(final Component activeMessage) {
      return ComponentUtils.mergeStyles(activeMessage, Style.EMPTY.withColor(-6250336));
   }

   public AbstractWidget$WithInactiveMessage(final int x, final int y, final int width, final int height, final Component message) {
      super(x, y, width, height, message);
      this.inactiveMessage = defaultInactiveMessage(message);
   }

   @Override
   public Component getMessage() {
      return this.active ? super.getMessage() : this.inactiveMessage;
   }

   @Override
   public void setMessage(final Component message) {
      super.setMessage(message);
      this.inactiveMessage = defaultInactiveMessage(message);
   }
}
