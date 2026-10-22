package net.minecraft.network.codec;

import com.mojang.serialization.Codec;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.RegistryOps;

class ByteBufCodecs$20 implements StreamCodec<RegistryFriendlyByteBuf, T> {
   ByteBufCodecs$20(final StreamCodec val$tagCodec, final Codec val$codec) {
      this.val$tagCodec = val$tagCodecx;
      this.val$codec = val$codecx;
   }

   public T decode(final RegistryFriendlyByteBuf input) {
      Tag tag = (Tag)this.val$tagCodec.decode(input);
      RegistryOps<Tag> ops = input.registryAccess().createSerializationContext(NbtOps.INSTANCE);
      return (T)this.val$codec.parse(ops, tag).getOrThrow(msg -> new DecoderException("Failed to decode: " + msg + " " + tag));
   }

   public void encode(final RegistryFriendlyByteBuf output, final T value) {
      RegistryOps<Tag> ops = output.registryAccess().createSerializationContext(NbtOps.INSTANCE);
      Tag tag = (Tag)this.val$codec.encodeStart(ops, value).getOrThrow(msg -> new EncoderException("Failed to encode: " + msg + " " + value));
      this.val$tagCodec.encode(output, tag);
   }
}
