package net.minecraft.client.gui.screens.advancements;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
record AdvancementTabType$Sprites(Identifier first, Identifier middle, Identifier last) {
}
