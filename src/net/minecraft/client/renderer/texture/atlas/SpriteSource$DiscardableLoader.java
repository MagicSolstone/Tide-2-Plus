package net.minecraft.client.renderer.texture.atlas;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface SpriteSource$DiscardableLoader extends SpriteSource$Loader {
   default void discard() {
   }
}
