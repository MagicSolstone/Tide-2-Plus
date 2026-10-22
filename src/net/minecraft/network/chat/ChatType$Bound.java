package net.minecraft.network.chat;

import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record ChatType$Bound(Holder<ChatType> chatType, Component name, Optional<Component> targetName) {
   public static final StreamCodec<RegistryFriendlyByteBuf, ChatType$Bound> STREAM_CODEC = StreamCodec.composite(
      ChatType.STREAM_CODEC,
      ChatType$Bound::chatType,
      ComponentSerialization.TRUSTED_STREAM_CODEC,
      ChatType$Bound::name,
      ComponentSerialization.TRUSTED_OPTIONAL_STREAM_CODEC,
      ChatType$Bound::targetName,
      ChatType$Bound::new
   );

   private ChatType$Bound(final Holder<ChatType> chatType, final Component name) {
      this(chatType, name, Optional.empty());
   }

   public Component decorate(final Component content) {
      return this.chatType.value().chat().decorate(content, this);
   }

   public Component decorateNarration(final Component content) {
      return this.chatType.value().narration().decorate(content, this);
   }

   public ChatType$Bound withTargetName(final Component targetName) {
      return new ChatType$Bound(this.chatType, this.name, Optional.of(targetName));
   }
}
