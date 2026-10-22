package net.minecraft.network.chat;

import java.util.UUID;
import java.util.function.BooleanSupplier;
import org.jspecify.annotations.Nullable;

@FunctionalInterface
public interface SignedMessageChain$Decoder {
   static SignedMessageChain$Decoder unsigned(final UUID profileId, final BooleanSupplier enforcesSecureChat) {
      return (signature, body) -> {
         if (enforcesSecureChat.getAsBoolean()) {
            throw new SignedMessageChain$DecodeException(SignedMessageChain$DecodeException.MISSING_PROFILE_KEY);
         } else {
            return PlayerChatMessage.unsigned(profileId, body.content());
         }
      };
   }

   PlayerChatMessage unpack(@Nullable MessageSignature signature, SignedMessageBody body) throws SignedMessageChain$DecodeException;

   default void setChainBroken() {
   }
}
