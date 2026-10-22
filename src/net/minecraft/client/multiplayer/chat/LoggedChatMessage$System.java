package net.minecraft.client.multiplayer.chat;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.time.Instant;
import java.util.UUID;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.util.ExtraCodecs;

@Environment(EnvType.CLIENT)
public record LoggedChatMessage$System(Component message, Instant timeStamp) implements LoggedChatMessage {
   public static final MapCodec<LoggedChatMessage$System> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            ComponentSerialization.CODEC.fieldOf("message").forGetter(LoggedChatMessage$System::message),
            ExtraCodecs.INSTANT_ISO8601.fieldOf("time_stamp").forGetter(LoggedChatMessage$System::timeStamp)
         )
         .apply(i, LoggedChatMessage$System::new)
   );

   @Override
   public Component toContentComponent() {
      return this.message;
   }

   @Override
   public boolean canReport(final UUID reportedPlayerId) {
      return false;
   }

   @Override
   public LoggedChatEvent$Type type() {
      return LoggedChatEvent$Type.SYSTEM;
   }
}
