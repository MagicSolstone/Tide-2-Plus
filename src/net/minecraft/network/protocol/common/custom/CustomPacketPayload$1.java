package net.minecraft.network.protocol.common.custom;

import java.util.Map;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

class CustomPacketPayload$1 implements StreamCodec<B, CustomPacketPayload> {
   CustomPacketPayload$1(final Map val$idToType, final CustomPacketPayload$FallbackProvider val$fallback) {
      this.val$idToType = val$idToTypex;
      this.val$fallback = val$fallbackx;
   }

   private StreamCodec<? super B, ? extends CustomPacketPayload> findCodec(final Identifier typeId) {
      StreamCodec<? super B, ? extends CustomPacketPayload> codec = (StreamCodec<? super B, ? extends CustomPacketPayload>)this.val$idToType.get(typeId);
      return codec != null ? codec : this.val$fallback.create(typeId);
   }

   private <T extends CustomPacketPayload> void writeCap(final B output, final CustomPacketPayload$Type<T> type, final CustomPacketPayload payload) {
      output.writeIdentifier(type.id());
      StreamCodec<B, T> codec = this.findCodec(type.id);
      codec.encode(output, (T)payload);
   }

   public void encode(final B output, final CustomPacketPayload value) {
      this.writeCap(output, value.type(), value);
   }

   public CustomPacketPayload decode(final B input) {
      Identifier identifier = input.readIdentifier();
      return (CustomPacketPayload)this.findCodec(identifier).decode(input);
   }
}
