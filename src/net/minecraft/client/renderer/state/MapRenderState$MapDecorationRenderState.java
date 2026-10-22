package net.minecraft.client.renderer.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class MapRenderState$MapDecorationRenderState {
   @Nullable
   public TextureAtlasSprite atlasSprite;
   public byte x;
   public byte y;
   public byte rot;
   public boolean renderOnFrame;
   @Nullable
   public Component name;
}
