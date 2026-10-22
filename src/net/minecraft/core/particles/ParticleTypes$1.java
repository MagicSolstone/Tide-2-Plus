package net.minecraft.core.particles;

import com.mojang.serialization.MapCodec;
import java.util.function.Function;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

class ParticleTypes$1 extends ParticleType<T> {
   ParticleTypes$1(final boolean overrideLimiter, final Function val$codec, final Function val$streamCodec) {
      super(overrideLimiter);
      this.val$codec = val$codecx;
      this.val$streamCodec = val$streamCodecx;
   }

   @Override
   public MapCodec<T> codec() {
      return (MapCodec<T>)this.val$codec.apply(this);
   }

   @Override
   public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
      return (StreamCodec<? super RegistryFriendlyByteBuf, T>)this.val$streamCodec.apply(this);
   }
}
