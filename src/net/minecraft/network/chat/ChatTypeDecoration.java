package net.minecraft.network.chat;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ChatTypeDecoration(String translationKey, List<ChatTypeDecoration$Parameter> parameters, Style style) {
   public static final Codec<ChatTypeDecoration> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Codec.STRING.fieldOf("translation_key").forGetter(ChatTypeDecoration::translationKey),
            ChatTypeDecoration$Parameter.CODEC.listOf().fieldOf("parameters").forGetter(ChatTypeDecoration::parameters),
            Style$Serializer.CODEC.optionalFieldOf("style", Style.EMPTY).forGetter(ChatTypeDecoration::style)
         )
         .apply(i, ChatTypeDecoration::new)
   );
   public static final StreamCodec<RegistryFriendlyByteBuf, ChatTypeDecoration> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.STRING_UTF8,
      ChatTypeDecoration::translationKey,
      ChatTypeDecoration$Parameter.STREAM_CODEC.apply(ByteBufCodecs.list()),
      ChatTypeDecoration::parameters,
      Style$Serializer.TRUSTED_STREAM_CODEC,
      ChatTypeDecoration::style,
      ChatTypeDecoration::new
   );

   public static ChatTypeDecoration withSender(final String translationKey) {
      return new ChatTypeDecoration(translationKey, List.of(ChatTypeDecoration$Parameter.SENDER, ChatTypeDecoration$Parameter.CONTENT), Style.EMPTY);
   }

   public static ChatTypeDecoration incomingDirectMessage(final String translationKey) {
      Style style = Style.EMPTY.withColor(ChatFormatting.GRAY).withItalic(true);
      return new ChatTypeDecoration(translationKey, List.of(ChatTypeDecoration$Parameter.SENDER, ChatTypeDecoration$Parameter.CONTENT), style);
   }

   public static ChatTypeDecoration outgoingDirectMessage(final String translationKey) {
      Style style = Style.EMPTY.withColor(ChatFormatting.GRAY).withItalic(true);
      return new ChatTypeDecoration(translationKey, List.of(ChatTypeDecoration$Parameter.TARGET, ChatTypeDecoration$Parameter.CONTENT), style);
   }

   public static ChatTypeDecoration teamMessage(final String translationKey) {
      return new ChatTypeDecoration(
         translationKey, List.of(ChatTypeDecoration$Parameter.TARGET, ChatTypeDecoration$Parameter.SENDER, ChatTypeDecoration$Parameter.CONTENT), Style.EMPTY
      );
   }

   public Component decorate(final Component content, final ChatType$Bound chatType) {
      Object[] parameters = this.resolveParameters(content, chatType);
      return Component.translatable(this.translationKey, parameters).withStyle(this.style);
   }

   private Component[] resolveParameters(final Component content, final ChatType$Bound chatType) {
      Component[] resolved = new Component[this.parameters.size()];

      for (int i = 0; i < resolved.length; i++) {
         ChatTypeDecoration$Parameter parameter = this.parameters.get(i);
         resolved[i] = parameter.select(content, chatType);
      }

      return resolved;
   }
}
