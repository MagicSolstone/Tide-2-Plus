package net.minecraft.client.resources.model.cuboid;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.model.UnbakedModel$GuiLight;
import net.minecraft.client.resources.model.geometry.UnbakedGeometry;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.minecraft.client.resources.model.sprite.TextureSlots$Data;
import net.minecraft.resources.Identifier;
import net.minecraft.util.GsonHelper;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class CuboidModel$Deserializer implements JsonDeserializer<CuboidModel> {
   public CuboidModel deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
      JsonObject object = json.getAsJsonObject();
      UnbakedGeometry elements = this.getElements(context, object);
      String parentName = this.getParentName(object);
      TextureSlots$Data textureMap = this.getTextureMap(object);
      Boolean hasAmbientOcclusion = this.getAmbientOcclusion(object);
      ItemTransforms transforms = null;
      if (object.has("display")) {
         JsonObject display = GsonHelper.getAsJsonObject(object, "display");
         transforms = (ItemTransforms)context.deserialize(display, ItemTransforms.class);
      }

      UnbakedModel$GuiLight guiLight = null;
      if (object.has("gui_light")) {
         guiLight = UnbakedModel$GuiLight.getByName(GsonHelper.getAsString(object, "gui_light"));
      }

      Identifier parentLocation = parentName.isEmpty() ? null : Identifier.parse(parentName);
      return new CuboidModel(elements, guiLight, hasAmbientOcclusion, transforms, textureMap, parentLocation);
   }

   private TextureSlots$Data getTextureMap(final JsonObject object) {
      if (object.has("textures")) {
         JsonObject texturesObject = GsonHelper.getAsJsonObject(object, "textures");
         return TextureSlots.parseTextureMap(texturesObject);
      } else {
         return TextureSlots$Data.EMPTY;
      }
   }

   private String getParentName(final JsonObject object) {
      return GsonHelper.getAsString(object, "parent", "");
   }

   @Nullable
   protected Boolean getAmbientOcclusion(final JsonObject object) {
      return object.has("ambientocclusion") ? GsonHelper.getAsBoolean(object, "ambientocclusion") : null;
   }

   @Nullable
   protected UnbakedGeometry getElements(final JsonDeserializationContext context, final JsonObject object) {
      if (!object.has("elements")) {
         return null;
      } else {
         List<CuboidModelElement> elements = new ArrayList<>();

         for (JsonElement element : GsonHelper.getAsJsonArray(object, "elements")) {
            elements.add((CuboidModelElement)context.deserialize(element, CuboidModelElement.class));
         }

         return new UnbakedCuboidGeometry(elements);
      }
   }
}
