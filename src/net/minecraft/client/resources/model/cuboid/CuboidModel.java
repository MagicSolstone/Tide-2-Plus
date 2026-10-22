package net.minecraft.client.resources.model.cuboid;

import com.google.common.annotations.VisibleForTesting;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.Reader;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.client.resources.model.UnbakedModel$GuiLight;
import net.minecraft.client.resources.model.geometry.UnbakedGeometry;
import net.minecraft.client.resources.model.sprite.TextureSlots$Data;
import net.minecraft.resources.Identifier;
import net.minecraft.util.GsonHelper;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record CuboidModel(
   @Nullable UnbakedGeometry geometry,
   @Nullable UnbakedModel$GuiLight guiLight,
   @Nullable Boolean ambientOcclusion,
   @Nullable ItemTransforms transforms,
   TextureSlots$Data textureSlots,
   @Nullable Identifier parent
) implements UnbakedModel {
   @VisibleForTesting
   static final Gson GSON = new GsonBuilder()
      .registerTypeAdapter(CuboidModel.class, new CuboidModel$Deserializer())
      .registerTypeAdapter(CuboidModelElement.class, new CuboidModelElement$Deserializer())
      .registerTypeAdapter(CuboidFace.class, new CuboidFace$Deserializer())
      .registerTypeAdapter(ItemTransform.class, new ItemTransform$Deserializer())
      .registerTypeAdapter(ItemTransforms.class, new ItemTransforms$Deserializer())
      .create();

   public static CuboidModel fromStream(final Reader reader) {
      return GsonHelper.fromJson(GSON, reader, CuboidModel.class);
   }
}
