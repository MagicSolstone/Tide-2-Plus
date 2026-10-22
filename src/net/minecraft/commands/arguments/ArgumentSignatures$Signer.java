package net.minecraft.commands.arguments;

import net.minecraft.network.chat.MessageSignature;
import org.jspecify.annotations.Nullable;

@FunctionalInterface
public interface ArgumentSignatures$Signer {
   @Nullable
   MessageSignature sign(String content);
}
