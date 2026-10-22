package net.minecraft.network.chat;

import com.mojang.logging.LogUtils;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@FunctionalInterface
public interface SignedMessageValidator {
   Logger LOGGER = LogUtils.getLogger();
   SignedMessageValidator ACCEPT_UNSIGNED = PlayerChatMessage::removeSignature;
   SignedMessageValidator REJECT_ALL = message -> {
      LOGGER.error("Received chat message from {}, but they have no chat session initialized and secure chat is enforced", message.sender());
      return null;
   };

   @Nullable
   PlayerChatMessage updateAndValidate(PlayerChatMessage message);
}
