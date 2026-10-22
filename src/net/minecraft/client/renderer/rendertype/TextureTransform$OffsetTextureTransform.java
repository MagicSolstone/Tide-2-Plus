package net.minecraft.client.renderer.rendertype;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Matrix4f;

@Environment(EnvType.CLIENT)
public final class TextureTransform$OffsetTextureTransform extends TextureTransform {
   public TextureTransform$OffsetTextureTransform(final float uOffset, final float vOffset) {
      super("offset_texturing", () -> new Matrix4f().translation(uOffset, vOffset, 0.0F));
   }
}
