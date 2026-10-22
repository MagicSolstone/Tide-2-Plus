package net.minecraft.network.syncher;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

class EntityDataSerializers$1 implements EntityDataSerializer<ItemStack> {
   @Override
   public StreamCodec<? super RegistryFriendlyByteBuf, ItemStack> codec() {
      return ItemStack.OPTIONAL_STREAM_CODEC;
   }

   public ItemStack copy(final ItemStack value) {
      return value.copy();
   }
}
