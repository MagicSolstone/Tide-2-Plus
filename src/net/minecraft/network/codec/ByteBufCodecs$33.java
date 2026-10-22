package net.minecraft.network.codec;

import net.minecraft.core.Holder;
import net.minecraft.core.IdMap;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.VarInt;
import net.minecraft.resources.ResourceKey;

class ByteBufCodecs$33 implements StreamCodec<RegistryFriendlyByteBuf, Holder<T>> {
   private static final int DIRECT_HOLDER_ID = 0;

   ByteBufCodecs$33(final ResourceKey val$registryKey, final StreamCodec val$directCodec) {
      this.val$registryKey = val$registryKeyx;
      this.val$directCodec = val$directCodecx;
   }

   private IdMap<Holder<T>> getRegistryOrThrow(final RegistryFriendlyByteBuf input) {
      return input.registryAccess().lookupOrThrow(this.val$registryKey).asHolderIdMap();
   }

   public Holder<T> decode(final RegistryFriendlyByteBuf input) {
      int id = VarInt.read(input);
      return (Holder<T>)(id == 0 ? Holder.direct(this.val$directCodec.decode(input)) : (Holder)this.getRegistryOrThrow(input).byIdOrThrow(id - 1));
   }

   public void encode(final RegistryFriendlyByteBuf output, final Holder<T> holder) {
      switch (holder.kind()) {
         case REFERENCE:
            int id = this.getRegistryOrThrow(output).getIdOrThrow(holder);
            VarInt.write(output, id + 1);
            break;
         case DIRECT:
            VarInt.write(output, 0);
            this.val$directCodec.encode(output, holder.value());
      }
   }
}
