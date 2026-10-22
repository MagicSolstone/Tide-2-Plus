package net.minecraft.network.protocol.game;

import java.util.function.Function;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

class GameProtocols$2 implements StreamCodec<RegistryFriendlyByteBuf, P> {
   GameProtocols$2(final Function val$codecGetter, final GameProtocols$Context val$context) {
      this.val$codecGetter = val$codecGetterx;
      this.val$context = val$contextx;
   }

   public P decode(final RegistryFriendlyByteBuf input) {
      return (P)((StreamCodec)this.val$codecGetter.apply(this.val$context)).decode(input);
   }

   public void encode(final RegistryFriendlyByteBuf output, final P value) {
      ((StreamCodec)this.val$codecGetter.apply(this.val$context)).encode(output, value);
   }
}
