package net.minecraft.client.resources.model.sprite;

import java.util.HashMap;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class TextureSlots$Data$Builder {
   private final Map<String, TextureSlots$SlotContents> textureMap = new HashMap<>();

   public TextureSlots$Data$Builder addReference(final String slot, final String reference) {
      this.textureMap.put(slot, new TextureSlots$Reference(reference));
      return this;
   }

   public TextureSlots$Data$Builder addTexture(final String slot, final Material material) {
      this.textureMap.put(slot, new TextureSlots$Value(material));
      return this;
   }

   public TextureSlots$Data build() {
      return this.textureMap.isEmpty() ? TextureSlots$Data.EMPTY : new TextureSlots$Data(Map.copyOf(this.textureMap));
   }
}
