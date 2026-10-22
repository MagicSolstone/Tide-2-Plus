package net.minecraft.world.entity.player;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import java.util.Optional;
import net.minecraft.core.ClientAsset$ResourceTexture;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record PlayerSkin$Patch(
   Optional<ClientAsset$ResourceTexture> body,
   Optional<ClientAsset$ResourceTexture> cape,
   Optional<ClientAsset$ResourceTexture> elytra,
   Optional<PlayerModelType> model
) {
   public static final PlayerSkin$Patch EMPTY = new PlayerSkin$Patch(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
   public static final MapCodec<PlayerSkin$Patch> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            ClientAsset$ResourceTexture.CODEC.optionalFieldOf("texture").forGetter(PlayerSkin$Patch::body),
            ClientAsset$ResourceTexture.CODEC.optionalFieldOf("cape").forGetter(PlayerSkin$Patch::cape),
            ClientAsset$ResourceTexture.CODEC.optionalFieldOf("elytra").forGetter(PlayerSkin$Patch::elytra),
            PlayerModelType.CODEC.optionalFieldOf("model").forGetter(PlayerSkin$Patch::model)
         )
         .apply(i, PlayerSkin$Patch::create)
   );
   public static final StreamCodec<ByteBuf, PlayerSkin$Patch> STREAM_CODEC = StreamCodec.composite(
      ClientAsset$ResourceTexture.STREAM_CODEC.apply(ByteBufCodecs::optional),
      PlayerSkin$Patch::body,
      ClientAsset$ResourceTexture.STREAM_CODEC.apply(ByteBufCodecs::optional),
      PlayerSkin$Patch::cape,
      ClientAsset$ResourceTexture.STREAM_CODEC.apply(ByteBufCodecs::optional),
      PlayerSkin$Patch::elytra,
      PlayerModelType.STREAM_CODEC.apply(ByteBufCodecs::optional),
      PlayerSkin$Patch::model,
      PlayerSkin$Patch::create
   );

   public static PlayerSkin$Patch create(
      final Optional<ClientAsset$ResourceTexture> texture,
      final Optional<ClientAsset$ResourceTexture> capeTexture,
      final Optional<ClientAsset$ResourceTexture> elytraTexture,
      final Optional<PlayerModelType> model
   ) {
      return texture.isEmpty() && capeTexture.isEmpty() && elytraTexture.isEmpty() && model.isEmpty()
         ? EMPTY
         : new PlayerSkin$Patch(texture, capeTexture, elytraTexture, model);
   }
}
