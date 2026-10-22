package net.minecraft.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;

@Environment(EnvType.CLIENT)
public record ComponentPath$Path(ContainerEventHandler component, ComponentPath childPath) implements ComponentPath {
   @Override
   public void applyFocus(final boolean focused) {
      if (!focused) {
         this.component.setFocused(null);
      } else {
         this.component.setFocused(this.childPath.component());
      }

      this.childPath.applyFocus(focused);
   }

   @Override
   public GuiEventListener leafComponent() {
      return this.childPath.leafComponent();
   }
}
