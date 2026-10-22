package net.minecraft.world.item.component;

import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Either;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.server.players.ProfileResolver;
import net.minecraft.world.entity.player.PlayerSkin$Patch;
import net.minecraft.world.item.Item$TooltipContext;
import net.minecraft.world.item.TooltipFlag;

public final class ResolvableProfile$Static extends ResolvableProfile {
   public static final ResolvableProfile$Static EMPTY = new ResolvableProfile$Static(Either.right(ResolvableProfile$Partial.EMPTY), PlayerSkin$Patch.EMPTY);
   private final Either<GameProfile, ResolvableProfile$Partial> contents;

   private ResolvableProfile$Static(final Either<GameProfile, ResolvableProfile$Partial> contents, final PlayerSkin$Patch skinPatch) {
      super((GameProfile)contents.map(gameProfile -> gameProfile, ResolvableProfile$Partial::createProfile), skinPatch);
      this.contents = contents;
   }

   @Override
   public CompletableFuture<GameProfile> resolveProfile(final ProfileResolver profileResolver) {
      return CompletableFuture.completedFuture(this.partialProfile);
   }

   @Override
   protected Either<GameProfile, ResolvableProfile$Partial> unpack() {
      return this.contents;
   }

   @Override
   public Optional<String> name() {
      return (Optional<String>)this.contents.map(gameProfile -> Optional.of(gameProfile.name()), partial -> partial.name);
   }

   @Override
   public boolean equals(final Object o) {
      return this == o || o instanceof ResolvableProfile$Static that && this.contents.equals(that.contents) && this.skinPatch.equals(that.skinPatch);
   }

   @Override
   public int hashCode() {
      int result = 31 + this.contents.hashCode();
      return 31 * result + this.skinPatch.hashCode();
   }

   @Override
   public void addToTooltip(final Item$TooltipContext context, final Consumer<Component> consumer, final TooltipFlag flag, final DataComponentGetter components) {
   }
}
