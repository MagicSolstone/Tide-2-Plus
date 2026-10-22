package net.minecraft.client.resources.model.sprite;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
record TextureSlots$Value(Material material) implements TextureSlots$SlotContents {
}
