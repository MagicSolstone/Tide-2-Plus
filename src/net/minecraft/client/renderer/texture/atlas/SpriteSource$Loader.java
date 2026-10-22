package net.minecraft.client.renderer.texture.atlas;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.SpriteContents;
import org.jspecify.annotations.Nullable;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface SpriteSource$Loader {
   @Nullable
   SpriteContents get(SpriteResourceLoader loader);
}
