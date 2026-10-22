package net.minecraft.client.gui.screens.options;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class MouseSettingsScreen extends OptionsSubScreen {
   private static final Component TITLE = Component.translatable("options.mouse_settings.title");

   private static OptionInstance<?>[] options(final Options options) {
      return new OptionInstance[]{
         options.sensitivity(),
         options.mouseWheelSensitivity(),
         options.discreteMouseScroll(),
         options.invertMouseX(),
         options.invertMouseY(),
         options.allowCursorChanges()
      };
   }

   public MouseSettingsScreen(final Screen lastScreen, final Options options) {
      super(lastScreen, options, TITLE);
   }

   @Override
   protected void addOptions() {
      this.list.addSmall(options(this.options));
   }
}
