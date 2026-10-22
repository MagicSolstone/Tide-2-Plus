package net.minecraft.world.item.component;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.datafixers.util.Either;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.server.players.ProfileResolver;
import net.minecraft.util.Util;
import net.minecraft.world.entity.player.PlayerSkin$Patch;
import net.minecraft.world.item.Item$TooltipContext;
import net.minecraft.world.item.TooltipFlag;

public final class ResolvableProfile$Dynamic extends ResolvableProfile {
   private static final Component DYNAMIC_TOOLTIP = Component.translatable("component.profile.dynamic").withStyle(ChatFormatting.GRAY);
   private final Either<String, UUID> nameOrId;

   private ResolvableProfile$Dynamic(final Either<String, UUID> nameOrId, final PlayerSkin$Patch skinPatch) {
      super(ResolvableProfile.createPartialProfile(nameOrId.left(), nameOrId.right(), PropertyMap.EMPTY), skinPatch);
      this.nameOrId = nameOrId;
   }

   @Override
   public Optional<String> name() {
      return this.nameOrId.left();
   }

   @Override
   public boolean equals(final Object o) {
      return this == o || o instanceof ResolvableProfile$Dynamic that && this.nameOrId.equals(that.nameOrId) && this.skinPatch.equals(that.skinPatch);
   }

   @Override
   public int hashCode() {
      int result = 31 + this.nameOrId.hashCode();
      return 31 * result + this.skinPatch.hashCode();
   }

   @Override
   protected Either<GameProfile, ResolvableProfile$Partial> unpack() {
      return Either.right(new ResolvableProfile$Partial(this.nameOrId.left(), this.nameOrId.right(), PropertyMap.EMPTY));
   }

   @Override
   public CompletableFuture<GameProfile> resolveProfile(final ProfileResolver profileResolver) {
      return CompletableFuture.supplyAsync(() -> profileResolver.fetchByNameOrId(this.nameOrId).orElse(this.partialProfile), Util.nonCriticalIoPool());
   }

   @Override
   public void addToTooltip(final Item$TooltipContext context, final Consumer<Component> consumer, final TooltipFlag flag, final DataComponentGetter components) {
      consumer.accept(DYNAMIC_TOOLTIP);
   }
}
