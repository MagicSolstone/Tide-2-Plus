package net.minecraft.client.resources.model.sprite;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.JsonOps;
import java.util.Map;
import java.util.Map.Entry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.GsonHelper;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class TextureSlots {
   public static final TextureSlots EMPTY = new TextureSlots(Map.of());
   private static final char REFERENCE_CHAR = '#';
   private final Map<String, Material> resolvedValues;

   private TextureSlots(final Map<String, Material> resolvedValues) {
      this.resolvedValues = resolvedValues;
   }

   @Nullable
   public Material getMaterial(String reference) {
      if (isTextureReference(reference)) {
         reference = reference.substring(1);
      }

      return this.resolvedValues.get(reference);
   }

   private static boolean isTextureReference(final String texture) {
      return texture.charAt(0) == '#';
   }

   public static TextureSlots$Data parseTextureMap(final JsonObject texturesObject) {
      TextureSlots$Data$Builder builder = new TextureSlots$Data$Builder();

      for (Entry<String, JsonElement> entry : texturesObject.entrySet()) {
         parseEntry(entry.getKey(), entry.getValue(), builder);
      }

      return builder.build();
   }

   private static void parseEntry(final String slot, final JsonElement value, final TextureSlots$Data$Builder output) {
      if (GsonHelper.isStringValue(value) && isTextureReference(value.getAsString())) {
         output.addReference(slot, value.getAsString().substring(1));
      } else {
         output.addTexture(slot, (Material)Material.CODEC.parse(JsonOps.INSTANCE, value).getOrThrow(JsonParseException::new));
      }
   }
}
