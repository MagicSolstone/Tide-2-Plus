package net.minecraft.network.protocol;

import io.netty.buffer.ByteBuf;
import java.util.function.Function;
import net.minecraft.network.PacketListener;
import net.minecraft.network.codec.StreamCodec;
import org.jspecify.annotations.Nullable;

record ProtocolInfoBuilder$CodecEntry<T extends PacketListener, P extends Packet<? super T>, B extends ByteBuf, C>(
   PacketType<P> type, StreamCodec<? super B, P> serializer, @Nullable CodecModifier<B, P, C> modifier
) {
   public void addToBuilder(final ProtocolCodecBuilder<ByteBuf, T> codecBuilder, final Function<ByteBuf, B> contextWrapper, final C context) {
      StreamCodec<? super B, P> finalSerializer;
      if (this.modifier != null) {
         finalSerializer = this.modifier.apply(this.serializer, context);
      } else {
         finalSerializer = this.serializer;
      }

      StreamCodec<ByteBuf, P> baseCodec = finalSerializer.mapStream(contextWrapper);
      codecBuilder.add(this.type, baseCodec);
   }
}
