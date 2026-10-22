package net.minecraft.client.gui.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.FormattedCharSequence;
import org.jspecify.annotations.Nullable;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface EditBox$TextFormatter {
   @Nullable
   FormattedCharSequence format(final String text, final int offset);
}
