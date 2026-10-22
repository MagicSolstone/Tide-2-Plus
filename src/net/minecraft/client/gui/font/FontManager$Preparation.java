package net.minecraft.client.gui.font;

import com.mojang.blaze3d.font.GlyphProvider;
import com.mojang.blaze3d.font.GlyphProvider$Conditional;
import java.util.List;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
record FontManager$Preparation(Map<Identifier, List<GlyphProvider$Conditional>> fontSets, List<GlyphProvider> allProviders) {
}
