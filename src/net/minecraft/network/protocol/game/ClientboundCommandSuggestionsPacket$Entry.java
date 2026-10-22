package net.minecraft.network.protocol.game;

import java.util.Optional;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ClientboundCommandSuggestionsPacket$Entry(String text, Optional<Component> tooltip) {
   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundCommandSuggestionsPacket$Entry> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.STRING_UTF8,
      ClientboundCommandSuggestionsPacket$Entry::text,
      ComponentSerialization.TRUSTED_OPTIONAL_STREAM_CODEC,
      ClientboundCommandSuggestionsPacket$Entry::tooltip,
      ClientboundCommandSuggestionsPacket$Entry::new
   );
}
