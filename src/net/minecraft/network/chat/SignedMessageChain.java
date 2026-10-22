package net.minecraft.network.chat;

import com.mojang.logging.LogUtils;
import java.time.Instant;
import java.util.UUID;
import net.minecraft.util.SignatureValidator;
import net.minecraft.util.Signer;
import net.minecraft.world.entity.player.ProfilePublicKey;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public class SignedMessageChain {
   private static final Logger LOGGER = LogUtils.getLogger();
   @Nullable
   private SignedMessageLink nextLink;
   private Instant lastTimeStamp = Instant.EPOCH;

   public SignedMessageChain(final UUID profileId, final UUID sessionId) {
      this.nextLink = SignedMessageLink.root(profileId, sessionId);
   }

   public SignedMessageChain$Encoder encoder(final Signer signer) {
      return body -> {
         SignedMessageLink link = this.nextLink;
         if (link == null) {
            return null;
         } else {
            this.nextLink = link.advance();
            return new MessageSignature(signer.sign(output -> PlayerChatMessage.updateSignature(output, link, body)));
         }
      };
   }

   public SignedMessageChain$Decoder decoder(final ProfilePublicKey profilePublicKey) {
      SignatureValidator signatureValidator = profilePublicKey.createSignatureValidator();
      return new SignedMessageChain$1(this, profilePublicKey, signatureValidator);
   }
}
