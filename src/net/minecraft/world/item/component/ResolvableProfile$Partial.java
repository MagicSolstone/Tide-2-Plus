package net.minecraft.world.item.component;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;

public record ResolvableProfile$Partial(Optional<String> name, Optional<UUID> id, PropertyMap properties) {
   public static final ResolvableProfile$Partial EMPTY = new ResolvableProfile$Partial(Optional.empty(), Optional.empty(), PropertyMap.EMPTY);
   private static final MapCodec<ResolvableProfile$Partial> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            ExtraCodecs.PLAYER_NAME.optionalFieldOf("name").forGetter(ResolvableProfile$Partial::name),
            UUIDUtil.CODEC.optionalFieldOf("id").forGetter(ResolvableProfile$Partial::id),
            ExtraCodecs.PROPERTY_MAP.optionalFieldOf("properties", PropertyMap.EMPTY).forGetter(ResolvableProfile$Partial::properties)
         )
         .apply(i, ResolvableProfile$Partial::new)
   );
   public static final StreamCodec<ByteBuf, ResolvableProfile$Partial> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.PLAYER_NAME.apply(ByteBufCodecs::optional),
      ResolvableProfile$Partial::name,
      UUIDUtil.STREAM_CODEC.apply(ByteBufCodecs::optional),
      ResolvableProfile$Partial::id,
      ByteBufCodecs.GAME_PROFILE_PROPERTIES,
      ResolvableProfile$Partial::properties,
      ResolvableProfile$Partial::new
   );

   private GameProfile createProfile() {
      return ResolvableProfile.createPartialProfile(this.name, this.id, this.properties);
   }
}
