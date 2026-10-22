package net.minecraft.core.component.predicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public abstract class DataComponentPredicate$TypeBase<T extends DataComponentPredicate> implements DataComponentPredicate$Type<T> {
   private final Codec<T> codec;
   private final MapCodec<DataComponentPredicate$Single<T>> wrappedCodec;
   private final StreamCodec<RegistryFriendlyByteBuf, DataComponentPredicate$Single<T>> singleStreamCodec;

   public DataComponentPredicate$TypeBase(final Codec<T> codec) {
      this.codec = codec;
      this.wrappedCodec = DataComponentPredicate$Single.wrapCodec(this, codec);
      this.singleStreamCodec = ByteBufCodecs.<DataComponentPredicate>fromCodecWithRegistries(codec)
         .map(v -> new DataComponentPredicate$Single<>(this, (T)v), DataComponentPredicate$Single::predicate);
   }

   @Override
   public Codec<T> codec() {
      return this.codec;
   }

   @Override
   public MapCodec<DataComponentPredicate$Single<T>> wrappedCodec() {
      return this.wrappedCodec;
   }

   @Override
   public StreamCodec<RegistryFriendlyByteBuf, DataComponentPredicate$Single<T>> singleStreamCodec() {
      return this.singleStreamCodec;
   }
}
