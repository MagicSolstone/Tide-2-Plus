package net.minecraft.client.resources.palette;

import com.mojang.blaze3d.platform.NativeImage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.metadata.texture.PaletteMetadataSection;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
record PalettedTextureManager$BaseTexture(NativeImage image, @Nullable PaletteMetadataSection paletteMetadata) implements AutoCloseable {
   @Override
   public void close() {
      this.image.close();
   }
}
