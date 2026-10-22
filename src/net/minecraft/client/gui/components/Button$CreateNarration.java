package net.minecraft.client.gui.components;

import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.MutableComponent;

@Environment(EnvType.CLIENT)
public interface Button$CreateNarration {
   MutableComponent createNarrationMessage(Supplier<MutableComponent> defaultNarrationSupplier);
}
