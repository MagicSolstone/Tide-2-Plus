package net.minecraft.network.chat;

import org.jspecify.annotations.Nullable;

@FunctionalInterface
public interface SignedMessageChain$Encoder {
   SignedMessageChain$Encoder UNSIGNED = body -> null;

   @Nullable
   MessageSignature pack(SignedMessageBody body);
}
