package net.minecraft.client.input;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record MouseButtonInfo(@MouseButtonInfo$MouseButton int button, @InputWithModifiers$Modifiers int modifiers) implements InputWithModifiers {
   @MouseButtonInfo$MouseButton
   @Override
   public int input() {
      return this.button;
   }
}
