package net.minecraft.client.gui.navigation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record FocusNavigationEvent$ArrowNavigation(ScreenDirection direction, @Nullable ScreenRectangle previousFocus) implements FocusNavigationEvent {
   public FocusNavigationEvent$ArrowNavigation(final ScreenDirection direction) {
      this(direction, null);
   }

   @Override
   public ScreenDirection getVerticalDirectionForInitialFocus() {
      return this.direction.getAxis() == ScreenAxis.VERTICAL ? this.direction : ScreenDirection.DOWN;
   }

   public FocusNavigationEvent$ArrowNavigation with(final ScreenRectangle previousFocus) {
      return new FocusNavigationEvent$ArrowNavigation(this.direction(), previousFocus);
   }
}
