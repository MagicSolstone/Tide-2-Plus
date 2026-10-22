package net.minecraft.world.item;

import io.netty.handler.codec.DecoderException;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryOps;
import net.minecraft.util.NullOps;
import net.minecraft.util.Unit;

class ItemStack$3 implements StreamCodec<RegistryFriendlyByteBuf, ItemStack> {
   ItemStack$3(final StreamCodec val$codec) {
      this.val$codec = val$codecx;
   }

   public ItemStack decode(final RegistryFriendlyByteBuf input) {
      ItemStack itemStack = (ItemStack)this.val$codec.decode(input);
      if (!itemStack.isEmpty()) {
         RegistryOps<Unit> ops = input.registryAccess().createSerializationContext(NullOps.INSTANCE);
         ItemStack.CODEC.encodeStart(ops, itemStack).getOrThrow(DecoderException::new);
      }

      return itemStack;
   }

   public void encode(final RegistryFriendlyByteBuf output, final ItemStack value) {
      this.val$codec.encode(output, value);
   }
}
