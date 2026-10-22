package net.minecraft.client.gui.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.OptionInstance;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record OptionsList$OptionInstanceWidget(AbstractWidget widget, @Nullable OptionInstance<?> optionInstance) {
   public OptionsList$OptionInstanceWidget(final AbstractWidget widget) {
      this(widget, null);
   }
}
