package net.minecraft.network.codec;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.VarInt;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

class ByteBufCodecs$34 implements StreamCodec<RegistryFriendlyByteBuf, HolderSet<T>> {
   private static final int NAMED_SET = -1;
   private final StreamCodec<RegistryFriendlyByteBuf, Holder<T>> holderCodec;

   ByteBufCodecs$34(final ResourceKey val$registryKey) {
      this.val$registryKey = val$registryKeyx;
      this.holderCodec = ByteBufCodecs.holderRegistry(this.val$registryKey);
   }

   public HolderSet<T> decode(final RegistryFriendlyByteBuf input) {
      int count = VarInt.read(input) - 1;
      if (count == -1) {
         Registry<T> registry = input.registryAccess().lookupOrThrow(this.val$registryKey);
         return (HolderSet<T>)registry.get(TagKey.create(this.val$registryKey, Identifier.STREAM_CODEC.decode(input))).orElseThrow();
      } else {
         List<Holder<T>> holders = new ArrayList<>(Math.min(count, 65536));

         for (int i = 0; i < count; i++) {
            holders.add((Holder)this.holderCodec.decode(input));
         }

         return HolderSet.direct(holders);
      }
   }

   public void encode(final RegistryFriendlyByteBuf output, final HolderSet<T> value) {
      Optional<TagKey<T>> key = value.unwrapKey();
      if (key.isPresent()) {
         VarInt.write(output, 0);
         Identifier.STREAM_CODEC.encode(output, ((TagKey)key.get()).location());
      } else {
         VarInt.write(output, value.size() + 1);

         for (Holder<T> holder : value) {
            this.holderCodec.encode(output, holder);
         }
      }
   }
}
