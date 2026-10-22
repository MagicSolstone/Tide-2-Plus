package net.minecraft.client.gui.navigation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class FocusNavigationEvent$InitialFocus implements FocusNavigationEvent {
   @Override
   public ScreenDirection getVerticalDirectionForInitialFocus() {
      return ScreenDirection.DOWN;
   }
}
