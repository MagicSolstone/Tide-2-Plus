package net.minecraft.client.gui.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
enum LockIconButton$Icon {
   LOCKED(Identifier.withDefaultNamespace("widget/locked_button")),
   LOCKED_HOVER(Identifier.withDefaultNamespace("widget/locked_button_highlighted")),
   LOCKED_DISABLED(Identifier.withDefaultNamespace("widget/locked_button_disabled")),
   UNLOCKED(Identifier.withDefaultNamespace("widget/unlocked_button")),
   UNLOCKED_HOVER(Identifier.withDefaultNamespace("widget/unlocked_button_highlighted")),
   UNLOCKED_DISABLED(Identifier.withDefaultNamespace("widget/unlocked_button_disabled"));

   private final Identifier sprite;

   private LockIconButton$Icon(final Identifier sprite) {
      this.sprite = sprite;
   }
}
