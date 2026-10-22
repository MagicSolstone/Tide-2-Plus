package net.minecraft.client.gui.screens;

import com.mojang.blaze3d.platform.NativeImage;
import java.io.IOException;
import java.io.InputStream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.MipmapStrategy;
import net.minecraft.client.renderer.texture.ReloadableTexture;
import net.minecraft.client.renderer.texture.TextureContents;
import net.minecraft.client.resources.metadata.texture.TextureMetadataSection;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceProvider;

@Environment(EnvType.CLIENT)
class LoadingOverlay$LogoTexture extends ReloadableTexture {
   public LoadingOverlay$LogoTexture() {
      super(LoadingOverlay.MOJANG_STUDIOS_LOGO_LOCATION);
   }

   @Override
   public TextureContents loadContents(final ResourceManager resourceManager) throws IOException {
      ResourceProvider vanillaProvider = Minecraft.getInstance().getVanillaPackResources().asResourceManager();

      TextureContents var4;
      try (InputStream resource = vanillaProvider.open(LoadingOverlay.MOJANG_STUDIOS_LOGO_LOCATION)) {
         var4 = new TextureContents(NativeImage.read(resource), new TextureMetadataSection(true, true, MipmapStrategy.MEAN, 0.0F));
      }

      return var4;
   }
}
