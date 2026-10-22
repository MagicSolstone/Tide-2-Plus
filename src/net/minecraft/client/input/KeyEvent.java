package net.minecraft.client.input;

import com.mojang.blaze3d.platform.InputConstants$Value;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record KeyEvent(@InputConstants$Value int key, int keycode, @InputWithModifiers$Modifiers int modifiers) implements InputWithModifiers {
   @Override
   public int input() {
      return this.key;
   }

   @Override
   public int shortcutKey() {
      return this.keycode;
   }
}
