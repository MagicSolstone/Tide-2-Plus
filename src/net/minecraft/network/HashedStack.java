package net.minecraft.network;

import com.mojang.datafixers.DataFixUtils;
import java.util.Optional;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

public interface HashedStack {
   HashedStack EMPTY = new HashedStack$1();
   StreamCodec<RegistryFriendlyByteBuf, HashedStack> STREAM_CODEC = ByteBufCodecs.optional(HashedStack$ActualItem.STREAM_CODEC)
      .map(
         actualItem -> (HashedStack)DataFixUtils.orElse(actualItem, EMPTY),
         hashedStack -> hashedStack instanceof HashedStack$ActualItem actualItem ? Optional.of(actualItem) : Optional.empty()
      );

   boolean matches(ItemStack stack, HashedPatchMap$HashGenerator hasher);

   static HashedStack create(final ItemStack itemStack, final HashedPatchMap$HashGenerator hasher) {
      return (HashedStack)(itemStack.isEmpty()
         ? EMPTY
         : new HashedStack$ActualItem(itemStack.typeHolder(), itemStack.getCount(), HashedPatchMap.create(itemStack.getComponentsPatch(), hasher)));
   }
}
