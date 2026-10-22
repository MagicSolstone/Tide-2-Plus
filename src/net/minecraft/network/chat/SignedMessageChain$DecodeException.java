package net.minecraft.network.chat;

public class SignedMessageChain$DecodeException extends ThrowingComponent {
   private static final Component MISSING_PROFILE_KEY = Component.translatable("chat.disabled.missingProfileKey");
   private static final Component CHAIN_BROKEN = Component.translatable("chat.disabled.chain_broken");
   private static final Component EXPIRED_PROFILE_KEY = Component.translatable("chat.disabled.expiredProfileKey");
   private static final Component INVALID_SIGNATURE = Component.translatable("chat.disabled.invalid_signature");
   private static final Component OUT_OF_ORDER_CHAT = Component.translatable("chat.disabled.out_of_order_chat");

   public SignedMessageChain$DecodeException(final Component component) {
      super(component);
   }
}
