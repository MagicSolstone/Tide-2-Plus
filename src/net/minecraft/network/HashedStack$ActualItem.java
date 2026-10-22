package net.minecraft.network;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record HashedStack$ActualItem(Holder<Item> item, int count, HashedPatchMap components) implements HashedStack {
   public static final StreamCodec<RegistryFriendlyByteBuf, HashedStack$ActualItem> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.holderRegistry(Registries.ITEM),
      HashedStack$ActualItem::item,
      ByteBufCodecs.VAR_INT,
      HashedStack$ActualItem::count,
      HashedPatchMap.STREAM_CODEC,
      HashedStack$ActualItem::components,
      HashedStack$ActualItem::new
   );

   @Override
   public boolean matches(final ItemStack itemStack, final HashedPatchMap$HashGenerator hasher) {
      if (this.count != itemStack.getCount()) {
         return false;
      } else {
         return !this.item.equals(itemStack.typeHolder()) ? false : this.components.matches(itemStack.getComponentsPatch(), hasher);
      }
   }
}
