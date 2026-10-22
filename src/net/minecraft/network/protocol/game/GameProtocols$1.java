package net.minecraft.network.protocol.game;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.SkipPacketDecoderException;
import net.minecraft.network.SkipPacketEncoderException;
import net.minecraft.network.codec.StreamCodec;

class GameProtocols$1 implements StreamCodec<RegistryFriendlyByteBuf, ServerboundSetCreativeModeSlotPacket> {
   GameProtocols$1(final GameProtocols$Context val$context, final StreamCodec val$original) {
      this.val$context = val$contextx;
      this.val$original = val$originalx;
   }

   public ServerboundSetCreativeModeSlotPacket decode(final RegistryFriendlyByteBuf input) {
      if (!this.val$context.hasInfiniteMaterials()) {
         throw new SkipPacketDecoderException("Not in creative mode");
      } else {
         return (ServerboundSetCreativeModeSlotPacket)this.val$original.decode(input);
      }
   }

   public void encode(final RegistryFriendlyByteBuf output, final ServerboundSetCreativeModeSlotPacket value) {
      if (!this.val$context.hasInfiniteMaterials()) {
         throw new SkipPacketEncoderException("Not in creative mode");
      } else {
         this.val$original.encode(output, value);
      }
   }
}
