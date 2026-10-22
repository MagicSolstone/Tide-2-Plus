package net.minecraft.client.resources.model.cuboid;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import org.joml.Vector3f;
import org.joml.Vector3fc;

@Environment(EnvType.CLIENT)
public class ItemTransform$Deserializer implements JsonDeserializer<ItemTransform> {
   private static final Vector3fc DEFAULT_ROTATION = new Vector3f();
   private static final Vector3fc DEFAULT_TRANSLATION = new Vector3f();
   private static final Vector3fc DEFAULT_SCALE = new Vector3f(1.0F, 1.0F, 1.0F);
   public static final float MAX_TRANSLATION = 5.0F;
   public static final float MAX_SCALE = 4.0F;

   protected ItemTransform$Deserializer() {
   }

   public ItemTransform deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
      JsonObject object = json.getAsJsonObject();
      Vector3f rotation = getVector3f(object, "rotation", DEFAULT_ROTATION);
      Vector3f translation = getVector3f(object, "translation", DEFAULT_TRANSLATION);
      translation.mul(0.0625F);
      translation.set(Mth.clamp(translation.x, -5.0F, 5.0F), Mth.clamp(translation.y, -5.0F, 5.0F), Mth.clamp(translation.z, -5.0F, 5.0F));
      Vector3f scale = getVector3f(object, "scale", DEFAULT_SCALE);
      scale.set(Mth.clamp(scale.x, -4.0F, 4.0F), Mth.clamp(scale.y, -4.0F, 4.0F), Mth.clamp(scale.z, -4.0F, 4.0F));
      return new ItemTransform(rotation, translation, scale);
   }

   private static Vector3f getVector3f(final JsonObject object, final String key, final Vector3fc def) {
      if (!object.has(key)) {
         return new Vector3f(def);
      } else {
         JsonArray vecArray = GsonHelper.getAsJsonArray(object, key);
         if (vecArray.size() != 3) {
            throw new JsonParseException("Expected 3 " + key + " values, found: " + vecArray.size());
         } else {
            float[] elements = new float[3];

            for (int i = 0; i < elements.length; i++) {
               elements[i] = GsonHelper.convertToFloat(vecArray.get(i), key + "[" + i + "]");
            }

            return new Vector3f(elements[0], elements[1], elements[2]);
         }
      }
   }
}
