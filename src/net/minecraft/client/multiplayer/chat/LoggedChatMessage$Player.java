package net.minecraft.client.multiplayer.chat;

import com.mojang.authlib.GameProfile;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.UUID;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Util;

@Environment(EnvType.CLIENT)
public record LoggedChatMessage$Player(GameProfile profile, PlayerChatMessage message, ChatTrustLevel trustLevel) implements LoggedChatMessage {
   public static final MapCodec<LoggedChatMessage$Player> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            ExtraCodecs.AUTHLIB_GAME_PROFILE.fieldOf("profile").forGetter(LoggedChatMessage$Player::profile),
            PlayerChatMessage.MAP_CODEC.forGetter(LoggedChatMessage$Player::message),
            ChatTrustLevel.CODEC.optionalFieldOf("trust_level", ChatTrustLevel.SECURE).forGetter(LoggedChatMessage$Player::trustLevel)
         )
         .apply(i, LoggedChatMessage$Player::new)
   );
   private static final DateTimeFormatter TIME_FORMATTER = Util.localizedDateFormatter(FormatStyle.SHORT);

   @Override
   public Component toContentComponent() {
      if (!this.message.filterMask().isEmpty()) {
         Component filtered = this.message.filterMask().applyWithFormatting(this.message.signedContent());
         return (Component)(filtered != null ? filtered : Component.empty());
      } else {
         return this.message.decoratedContent();
      }
   }

   @Override
   public Component toNarrationComponent() {
      Component content = this.toContentComponent();
      Component time = this.getTimeComponent();
      return Component.translatable("gui.chatSelection.message.narrate", this.profile.name(), content, time);
   }

   public Component toHeadingComponent() {
      Component time = this.getTimeComponent();
      return Component.translatable("gui.chatSelection.heading", this.profile.name(), time);
   }

   private Component getTimeComponent() {
      ZonedDateTime dateTime = ZonedDateTime.ofInstant(this.message.timeStamp(), ZoneId.systemDefault());
      return Component.literal(dateTime.format(TIME_FORMATTER)).withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY);
   }

   @Override
   public boolean canReport(final UUID reportedPlayerId) {
      return this.message.hasSignatureFrom(reportedPlayerId);
   }

   public UUID profileId() {
      return this.profile.id();
   }

   @Override
   public LoggedChatEvent$Type type() {
      return LoggedChatEvent$Type.PLAYER;
   }
}
