package net.minecraft.network.codec;

import io.netty.buffer.ByteBuf;
import java.util.Optional;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

class ByteBufCodecs$21 implements StreamCodec<ByteBuf, Optional<CompoundTag>> {
   public Optional<CompoundTag> decode(final ByteBuf input) {
      return Optional.ofNullable(FriendlyByteBuf.readNbt(input));
   }

   public void encode(final ByteBuf output, final Optional<CompoundTag> value) {
      FriendlyByteBuf.writeNbt(output, value.orElse(null));
   }
}
