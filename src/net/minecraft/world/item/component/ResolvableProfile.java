package net.minecraft.world.item.component;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.players.ProfileResolver;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Util;
import net.minecraft.world.entity.player.PlayerSkin$Patch;

public abstract sealed class ResolvableProfile implements TooltipProvider permits ResolvableProfile$Static, ResolvableProfile$Dynamic {
   private static final Codec<ResolvableProfile> FULL_CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Codec.mapEither(ExtraCodecs.STORED_GAME_PROFILE, ResolvableProfile$Partial.MAP_CODEC).forGetter(ResolvableProfile::unpack),
            PlayerSkin$Patch.MAP_CODEC.forGetter(ResolvableProfile::skinPatch)
         )
         .apply(i, ResolvableProfile::create)
   );
   public static final Codec<ResolvableProfile> CODEC = Codec.withAlternative(FULL_CODEC, ExtraCodecs.PLAYER_NAME, ResolvableProfile::createUnresolved);
   public static final StreamCodec<ByteBuf, ResolvableProfile> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.either(ByteBufCodecs.GAME_PROFILE, ResolvableProfile$Partial.STREAM_CODEC),
      ResolvableProfile::unpack,
      PlayerSkin$Patch.STREAM_CODEC,
      ResolvableProfile::skinPatch,
      ResolvableProfile::create
   );
   protected final GameProfile partialProfile;
   protected final PlayerSkin$Patch skinPatch;

   private static ResolvableProfile create(final Either<GameProfile, ResolvableProfile$Partial> value, final PlayerSkin$Patch patch) {
      return (ResolvableProfile)value.map(
         full -> new ResolvableProfile$Static(Either.left(full), patch),
         partial -> (ResolvableProfile)(partial.properties.isEmpty() && partial.id.isPresent() != partial.name.isPresent()
            ? partial.name
               .<ResolvableProfile>map(s -> new ResolvableProfile$Dynamic(Either.left(s), patch))
               .orElseGet(() -> new ResolvableProfile$Dynamic(Either.right(partial.id.get()), patch))
            : new ResolvableProfile$Static(Either.right(partial), patch))
      );
   }

   public static ResolvableProfile createResolved(final GameProfile gameProfile) {
      return new ResolvableProfile$Static(Either.left(gameProfile), PlayerSkin$Patch.EMPTY);
   }

   public static ResolvableProfile createUnresolved(final String name) {
      return new ResolvableProfile$Dynamic(Either.left(name), PlayerSkin$Patch.EMPTY);
   }

   public static ResolvableProfile createUnresolved(final UUID id) {
      return new ResolvableProfile$Dynamic(Either.right(id), PlayerSkin$Patch.EMPTY);
   }

   protected abstract Either<GameProfile, ResolvableProfile$Partial> unpack();

   protected ResolvableProfile(final GameProfile partialProfile, final PlayerSkin$Patch skinPatch) {
      this.partialProfile = partialProfile;
      this.skinPatch = skinPatch;
   }

   public abstract CompletableFuture<GameProfile> resolveProfile(ProfileResolver profileResolver);

   public GameProfile partialProfile() {
      return this.partialProfile;
   }

   public PlayerSkin$Patch skinPatch() {
      return this.skinPatch;
   }

   private static GameProfile createPartialProfile(final Optional<String> maybeName, final Optional<UUID> maybeId, final PropertyMap properties) {
      String name = maybeName.orElse("");
      UUID id = maybeId.orElseGet(() -> maybeName.map(UUIDUtil::createOfflinePlayerUUID).orElse(Util.NIL_UUID));
      return new GameProfile(id, name, properties);
   }

   public abstract Optional<String> name();
}
