package net.minecraft.network.codec;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import java.util.function.Supplier;
import net.minecraft.nbt.EndTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

class ByteBufCodecs$18 implements StreamCodec<ByteBuf, Tag> {
   ByteBufCodecs$18(final Supplier val$accounter) {
      this.val$accounter = val$accounterx;
   }

   public Tag decode(final ByteBuf input) {
      Tag result = FriendlyByteBuf.readNbt(input, (NbtAccounter)this.val$accounter.get());
      if (result == null) {
         throw new DecoderException("Expected non-null compound tag");
      } else {
         return result;
      }
   }

   public void encode(final ByteBuf output, final Tag value) {
      if (value == EndTag.INSTANCE) {
         throw new EncoderException("Expected non-null compound tag");
      } else {
         FriendlyByteBuf.writeNbt(output, value);
      }
   }
}
