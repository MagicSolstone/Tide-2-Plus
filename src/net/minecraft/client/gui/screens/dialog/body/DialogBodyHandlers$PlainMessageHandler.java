package net.minecraft.client.gui.screens.dialog.body;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.FocusableTextWidget;
import net.minecraft.client.gui.components.FocusableTextWidget$BackgroundFill;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.screens.dialog.DialogScreen;
import net.minecraft.server.dialog.body.PlainMessage;

@Environment(EnvType.CLIENT)
class DialogBodyHandlers$PlainMessageHandler implements DialogBodyHandler<PlainMessage> {
   private DialogBodyHandlers$PlainMessageHandler() {
   }

   public LayoutElement createControls(final DialogScreen<?> parent, final PlainMessage message) {
      return FocusableTextWidget.builder(message.contents(), parent.getFont())
         .maxWidth(message.width())
         .alwaysShowBorder(false)
         .backgroundFill(FocusableTextWidget$BackgroundFill.NEVER)
         .build()
         .setCentered(true)
         .setComponentClickHandler(style -> DialogBodyHandlers.runActionOnParent(parent, style));
   }
}
