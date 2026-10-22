package net.minecraft.network.codec;

import java.util.function.Function;
import net.minecraft.core.IdMap;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.VarInt;
import net.minecraft.resources.ResourceKey;

class ByteBufCodecs$32 implements StreamCodec<RegistryFriendlyByteBuf, R> {
   ByteBufCodecs$32(final Function val$mapExtractor, final ResourceKey val$registryKey) {
      this.val$mapExtractor = val$mapExtractorx;
      this.val$registryKey = val$registryKeyx;
   }

   private IdMap<R> getRegistryOrThrow(final RegistryFriendlyByteBuf input) {
      return (IdMap<R>)this.val$mapExtractor.apply(input.registryAccess().lookupOrThrow(this.val$registryKey));
   }

   public R decode(final RegistryFriendlyByteBuf input) {
      int id = VarInt.read(input);
      return (R)this.getRegistryOrThrow(input).byIdOrThrow(id);
   }

   public void encode(final RegistryFriendlyByteBuf output, final R value) {
      int id = this.getRegistryOrThrow(output).getIdOrThrow(value);
      VarInt.write(output, id);
   }
}
