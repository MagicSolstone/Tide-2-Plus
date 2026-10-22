package net.minecraft.network.codec;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import java.util.function.BiFunction;
import net.minecraft.network.VarInt;

class ByteBufCodecs$30 implements StreamCodec<B, V> {
   ByteBufCodecs$30(final int val$maxSize, final BiFunction val$decorator, final StreamCodec val$original) {
      this.val$maxSize = val$maxSizex;
      this.val$decorator = val$decoratorx;
      this.val$original = val$originalx;
   }

   public V decode(final B input) {
      int size = VarInt.read(input);
      if (size > this.val$maxSize) {
         throw new DecoderException("Buffer size " + size + " is larger than allowed limit of " + this.val$maxSize);
      } else {
         int index = input.readerIndex();
         B limitedSlice = (B)((ByteBuf)this.val$decorator.apply(input, input.slice(index, size)));
         input.readerIndex(index + size);
         return (V)this.val$original.decode(limitedSlice);
      }
   }

   public void encode(final B output, final V value) {
      B scratchBuffer = (B)((ByteBuf)this.val$decorator.apply(output, output.alloc().buffer()));

      try {
         this.val$original.encode(scratchBuffer, value);
         int size = scratchBuffer.readableBytes();
         if (size > this.val$maxSize) {
            throw new EncoderException("Buffer size " + size + " is  larger than allowed limit of " + this.val$maxSize);
         }

         VarInt.write(output, size);
         output.writeBytes(scratchBuffer);
      } finally {
         scratchBuffer.release();
      }
   }
}
