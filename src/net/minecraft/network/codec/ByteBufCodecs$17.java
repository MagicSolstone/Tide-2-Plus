package net.minecraft.network.codec;

import io.netty.buffer.ByteBuf;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

class ByteBufCodecs$17 implements StreamCodec<ByteBuf, Optional<Tag>> {
   ByteBufCodecs$17(final Supplier val$accounter) {
      this.val$accounter = val$accounterx;
   }

   public Optional<Tag> decode(final ByteBuf input) {
      return Optional.ofNullable(FriendlyByteBuf.readNbt(input, (NbtAccounter)this.val$accounter.get()));
   }

   public void encode(final ByteBuf output, final Optional<Tag> value) {
      FriendlyByteBuf.writeNbt(output, value.orElse(null));
   }
}
