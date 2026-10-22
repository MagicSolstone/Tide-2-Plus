package com.mojang.realmsclient.util;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;
import org.lwjgl.system.MemoryUtil;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class RealmsTextureManager {
   private static final Map<String, RealmsTextureManager$RealmsTexture> TEXTURES = Maps.newHashMap();
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final Identifier TEMPLATE_ICON_LOCATION = Identifier.withDefaultNamespace("textures/gui/presets/isles.png");

   public static Identifier worldTemplate(final String id, @Nullable final String image) {
      return image == null ? TEMPLATE_ICON_LOCATION : getTexture(id, image);
   }

   private static Identifier getTexture(final String id, final String encodedImage) {
      RealmsTextureManager$RealmsTexture texture = TEXTURES.get(id);
      if (texture != null && texture.image().equals(encodedImage)) {
         return texture.textureId;
      } else {
         NativeImage image = loadImage(encodedImage);
         if (image == null) {
            Identifier missingTexture = MissingTextureAtlasSprite.getLocation();
            TEXTURES.put(id, new RealmsTextureManager$RealmsTexture(encodedImage, missingTexture));
            return missingTexture;
         } else {
            Identifier textureId = Identifier.fromNamespaceAndPath("realms", "dynamic/" + id);
            Minecraft.getInstance().getTextureManager().register(textureId, new DynamicTexture(textureId::toString, image));
            TEXTURES.put(id, new RealmsTextureManager$RealmsTexture(encodedImage, textureId));
            return textureId;
         }
      }
   }

   @Nullable
   private static NativeImage loadImage(final String encodedImage) {
      byte[] bytes = Base64.getDecoder().decode(encodedImage);
      ByteBuffer buffer = MemoryUtil.memAlloc(bytes.length);

      try {
         return NativeImage.read(buffer.put(bytes).flip());
      } catch (IOException var7) {
         LOGGER.warn("Failed to load world image: {}", encodedImage, var7);
      } finally {
         MemoryUtil.memFree(buffer);
      }

      return null;
   }
}
