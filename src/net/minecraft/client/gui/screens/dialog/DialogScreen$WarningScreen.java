package net.minecraft.client.gui.screens.dialog;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class DialogScreen$WarningScreen extends ConfirmScreen {
   private final MutableObject<Screen> returnScreen;

   public static Screen create(final Minecraft minecraft, final DialogConnectionAccess connectionAccess, final Screen returnScreen) {
      return new DialogScreen$WarningScreen(minecraft, connectionAccess, new MutableObject(returnScreen));
   }

   private DialogScreen$WarningScreen(final Minecraft minecraft, final DialogConnectionAccess connectionAccess, final MutableObject<Screen> returnScreen) {
      super(
         disconnect -> {
            if (disconnect) {
               connectionAccess.disconnect(DialogScreen.DISCONNECT);
            } else {
               minecraft.gui.setScreen((Screen)returnScreen.get());
            }
         },
         Component.translatable("menu.custom_screen_info.title"),
         Component.translatable("menu.custom_screen_info.contents"),
         CommonComponents.disconnectButtonLabel(minecraft.isLocalServer()),
         CommonComponents.GUI_BACK
      );
      this.returnScreen = returnScreen;
   }

   @Nullable
   public Screen returnScreen() {
      return (Screen)this.returnScreen.get();
   }

   public void updateReturnScreen(@Nullable final Screen newReturnScreen) {
      this.returnScreen.setValue(newReturnScreen);
   }
}
