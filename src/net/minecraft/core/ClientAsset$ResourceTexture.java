package net.minecraft.core;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

public record ClientAsset$ResourceTexture(Identifier id, Identifier texturePath) implements ClientAsset$Texture {
   public static final Codec<ClientAsset$ResourceTexture> CODEC = Identifier.CODEC.xmap(ClientAsset$ResourceTexture::new, ClientAsset$ResourceTexture::id);
   public static final MapCodec<ClientAsset$ResourceTexture> DEFAULT_FIELD_CODEC = CODEC.fieldOf("asset_id");
   public static final StreamCodec<ByteBuf, ClientAsset$ResourceTexture> STREAM_CODEC = Identifier.STREAM_CODEC
      .map(ClientAsset$ResourceTexture::new, ClientAsset$ResourceTexture::id);

   public ClientAsset$ResourceTexture(final Identifier texture) {
      this(texture, texture.withPath(path -> "textures/" + path + ".png"));
   }
}
