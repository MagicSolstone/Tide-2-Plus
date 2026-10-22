package net.minecraft.network.protocol;

import io.netty.buffer.ByteBuf;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import net.minecraft.network.ProtocolInfo;
import net.minecraft.network.ProtocolInfo$Details;

class ProtocolInfoBuilder$3 implements UnboundProtocol<T, B, C> {
   ProtocolInfoBuilder$3(final ProtocolInfoBuilder this$0, final List val$codecs, final BundlerInfo val$bundlerInfo, final ProtocolInfo$Details val$details) {
      this.val$codecs = val$codecsx;
      this.val$bundlerInfo = val$bundlerInfox;
      this.val$details = val$detailsx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public ProtocolInfo<T> bind(final Function<ByteBuf, B> contextWrapper, final C context) {
      return new ProtocolInfoBuilder$Implementation(
         this.this$0.protocol, this.this$0.flow, this.this$0.buildPacketCodec(contextWrapper, this.val$codecs, context), this.val$bundlerInfo
      );
   }

   @Override
   public ProtocolInfo$Details details() {
      return this.val$details;
   }
}
