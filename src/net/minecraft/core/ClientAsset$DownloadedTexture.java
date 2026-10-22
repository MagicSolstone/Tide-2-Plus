package net.minecraft.core;

import net.minecraft.resources.Identifier;

public record ClientAsset$DownloadedTexture(Identifier texturePath, String url) implements ClientAsset$Texture {
   @Override
   public Identifier id() {
      return this.texturePath;
   }
}
