package net.minecraft.client.resources.model.sprite;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public sealed interface TextureSlots$SlotContents permits TextureSlots$Value, TextureSlots$Reference {
}
