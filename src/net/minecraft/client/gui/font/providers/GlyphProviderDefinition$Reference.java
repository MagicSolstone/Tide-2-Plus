package net.minecraft.client.gui.font.providers;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public record GlyphProviderDefinition$Reference(Identifier id) {
}
