package net.minecraft.network.chat;

import java.time.Instant;
import java.util.Objects;
import net.minecraft.util.SignatureValidator;
import net.minecraft.world.entity.player.ProfilePublicKey;
import org.jspecify.annotations.Nullable;

class SignedMessageChain$1 implements SignedMessageChain$Decoder {
   SignedMessageChain$1(final SignedMessageChain this$0, final ProfilePublicKey val$profilePublicKey, final SignatureValidator val$signatureValidator) {
      this.val$profilePublicKey = val$profilePublicKeyx;
      this.val$signatureValidator = val$signatureValidatorx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public PlayerChatMessage unpack(@Nullable final MessageSignature signature, final SignedMessageBody body) throws SignedMessageChain$DecodeException {
      if (signature == null) {
         throw new SignedMessageChain$DecodeException(SignedMessageChain$DecodeException.MISSING_PROFILE_KEY);
      } else if (this.val$profilePublicKey.data().hasExpired()) {
         throw new SignedMessageChain$DecodeException(SignedMessageChain$DecodeException.EXPIRED_PROFILE_KEY);
      } else {
         SignedMessageLink link = this.this$0.nextLink;
         if (link == null) {
            throw new SignedMessageChain$DecodeException(SignedMessageChain$DecodeException.CHAIN_BROKEN);
         } else if (body.timeStamp().isBefore(this.this$0.lastTimeStamp)) {
            this.setChainBroken();
            throw new SignedMessageChain$DecodeException(SignedMessageChain$DecodeException.OUT_OF_ORDER_CHAT);
         } else {
            this.this$0.lastTimeStamp = body.timeStamp();
            PlayerChatMessage unpacked = new PlayerChatMessage(link, signature, body, null, FilterMask.PASS_THROUGH);
            if (!unpacked.verify(this.val$signatureValidator)) {
               this.setChainBroken();
               throw new SignedMessageChain$DecodeException(SignedMessageChain$DecodeException.INVALID_SIGNATURE);
            } else {
               if (unpacked.hasExpiredServer(Instant.now())) {
                  SignedMessageChain.LOGGER.warn("Received expired chat: '{}'. Is the client/server system time unsynchronized?", body.content());
               }

               this.this$0.nextLink = link.advance();
               return unpacked;
            }
         }
      }
   }

   @Override
   public void setChainBroken() {
      this.this$0.nextLink = null;
   }
}
