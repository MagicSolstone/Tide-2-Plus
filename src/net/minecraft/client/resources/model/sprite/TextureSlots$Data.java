package net.minecraft.client.resources.model.sprite;

import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record TextureSlots$Data(Map<String, TextureSlots$SlotContents> values) {
   public static final TextureSlots$Data EMPTY = new TextureSlots$Data(Map.of());
}
