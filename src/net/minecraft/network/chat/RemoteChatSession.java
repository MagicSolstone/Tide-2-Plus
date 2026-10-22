package net.minecraft.network.chat;

import java.time.Duration;
import java.util.UUID;
import net.minecraft.world.entity.player.ProfilePublicKey;

public record RemoteChatSession(UUID sessionId, ProfilePublicKey profilePublicKey) {
   public SignedMessageValidator createMessageValidator(final Duration gracePeriod) {
      return new SignedMessageValidator$KeyBased(this.profilePublicKey.createSignatureValidator(), () -> this.profilePublicKey.data().hasExpired(gracePeriod));
   }

   public SignedMessageChain$Decoder createMessageDecoder(final UUID profileId) {
      return new SignedMessageChain(profileId, this.sessionId).decoder(this.profilePublicKey);
   }

   public RemoteChatSession$Data asData() {
      return new RemoteChatSession$Data(this.sessionId, this.profilePublicKey.data());
   }

   public boolean hasExpired() {
      return this.profilePublicKey.data().hasExpired();
   }
}
