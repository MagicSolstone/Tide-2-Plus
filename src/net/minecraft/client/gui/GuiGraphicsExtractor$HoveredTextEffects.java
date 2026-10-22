package net.minecraft.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum GuiGraphicsExtractor$HoveredTextEffects {
   NONE(false, false),
   TOOLTIP_ONLY(true, false),
   TOOLTIP_AND_CURSOR(true, true);

   public final boolean allowTooltip;
   public final boolean allowCursorChanges;

   private GuiGraphicsExtractor$HoveredTextEffects(final boolean allowTooltip, final boolean allowCursorChanges) {
      this.allowTooltip = allowTooltip;
      this.allowCursorChanges = allowCursorChanges;
   }

   public static GuiGraphicsExtractor$HoveredTextEffects notClickable(final boolean canTooltip) {
      return canTooltip ? TOOLTIP_ONLY : NONE;
   }
}
