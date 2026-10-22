package net.minecraft.client.gui.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Options;
import net.minecraft.network.chat.CommonComponents;

@Environment(EnvType.CLIENT)
public abstract class AbstractOptionSliderButton extends AbstractSliderButton {
   protected final Options options;

   protected AbstractOptionSliderButton(final Options options, final int x, final int y, final int width, final int height, final double initialValue) {
      super(x, y, width, height, CommonComponents.EMPTY, initialValue);
      this.options = options;
   }
}
