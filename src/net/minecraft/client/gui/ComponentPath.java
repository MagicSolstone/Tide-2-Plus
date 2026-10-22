package net.minecraft.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public interface ComponentPath {
   static ComponentPath leaf(final GuiEventListener component) {
      return new ComponentPath$Leaf(component);
   }

   @Nullable
   static ComponentPath path(final ContainerEventHandler container, @Nullable final ComponentPath childPath) {
      return childPath == null ? null : new ComponentPath$Path(container, childPath);
   }

   static ComponentPath path(final GuiEventListener target, final ContainerEventHandler... containerPath) {
      ComponentPath path = leaf(target);

      for (ContainerEventHandler container : containerPath) {
         path = path(container, path);
      }

      return path;
   }

   GuiEventListener component();

   void applyFocus(boolean focused);

   GuiEventListener leafComponent();
}
