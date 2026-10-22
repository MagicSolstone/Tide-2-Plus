package net.minecraft.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.events.GuiEventListener;

@Environment(EnvType.CLIENT)
public record ComponentPath$Leaf(GuiEventListener component) implements ComponentPath {
   @Override
   public void applyFocus(final boolean focused) {
      this.component.setFocused(focused);
   }

   @Override
   public GuiEventListener leafComponent() {
      return this.component;
   }
}
