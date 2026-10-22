package net.minecraft.client.gui.navigation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record FocusNavigationEvent$TabNavigation(boolean forward) implements FocusNavigationEvent {
   @Override
   public ScreenDirection getVerticalDirectionForInitialFocus() {
      return this.forward ? ScreenDirection.DOWN : ScreenDirection.UP;
   }
}
