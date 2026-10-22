package net.minecraft.world.item;

import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

class ItemStack$2 implements StreamCodec<RegistryFriendlyByteBuf, ItemStack> {
   public ItemStack decode(final RegistryFriendlyByteBuf input) {
      ItemStack itemStack = ItemStack.OPTIONAL_STREAM_CODEC.decode(input);
      if (itemStack.isEmpty()) {
         throw new DecoderException("Empty ItemStack not allowed");
      } else {
         return itemStack;
      }
   }

   public void encode(final RegistryFriendlyByteBuf output, final ItemStack itemStack) {
      if (itemStack.isEmpty()) {
         throw new EncoderException("Empty ItemStack not allowed");
      } else {
         ItemStack.OPTIONAL_STREAM_CODEC.encode(output, itemStack);
      }
   }
}
