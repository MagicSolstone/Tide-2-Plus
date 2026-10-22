package net.minecraft.client.gui.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record AbstractScrollArea$ScrollbarSettings(
   Identifier scrollerSprite,
   @Nullable Identifier disabledScrollerSprite,
   Identifier backgroundSprite,
   int scrollbarWidth,
   int scrollbarMinHeight,
   int scrollRate,
   boolean resizingScrollbar
) {
   public static final AbstractScrollArea$ScrollbarSettings NO_SCROLL = AbstractScrollArea.defaultSettings(0);
}
