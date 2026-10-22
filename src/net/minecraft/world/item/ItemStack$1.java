package net.minecraft.world.item;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

class ItemStack$1 implements StreamCodec<RegistryFriendlyByteBuf, ItemStack> {
   ItemStack$1(final StreamCodec val$patchCodec) {
      this.val$patchCodec = val$patchCodecx;
   }

   public ItemStack decode(final RegistryFriendlyByteBuf input) {
      int count = input.readVarInt();
      if (count <= 0) {
         return ItemStack.EMPTY;
      } else {
         Holder<Item> item = Item.STREAM_CODEC.decode(input);
         DataComponentPatch patch = (DataComponentPatch)this.val$patchCodec.decode(input);
         return new ItemStack(item, count, patch);
      }
   }

   public void encode(final RegistryFriendlyByteBuf output, final ItemStack itemStack) {
      if (itemStack.isEmpty()) {
         output.writeVarInt(0);
      } else {
         output.writeVarInt(itemStack.getCount());
         Item.STREAM_CODEC.encode(output, itemStack.typeHolder());
         this.val$patchCodec.encode(output, itemStack.components.asPatch());
      }
   }
}
