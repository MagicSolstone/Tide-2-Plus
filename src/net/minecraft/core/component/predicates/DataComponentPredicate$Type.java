package net.minecraft.core.component.predicates;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public interface DataComponentPredicate$Type<T extends DataComponentPredicate> {
   Codec<DataComponentPredicate$Type<?>> CODEC = Codec.either(
         BuiltInRegistries.DATA_COMPONENT_PREDICATE_TYPE.byNameCodec(), BuiltInRegistries.DATA_COMPONENT_TYPE.byNameCodec()
      )
      .xmap(DataComponentPredicate$Type::copyOrCreateType, DataComponentPredicate$Type::unpackType);
   StreamCodec<RegistryFriendlyByteBuf, DataComponentPredicate$Type<?>> STREAM_CODEC = ByteBufCodecs.either(
         ByteBufCodecs.registry(Registries.DATA_COMPONENT_PREDICATE_TYPE), ByteBufCodecs.registry(Registries.DATA_COMPONENT_TYPE)
      )
      .map(DataComponentPredicate$Type::copyOrCreateType, DataComponentPredicate$Type::unpackType);

   private static <T extends DataComponentPredicate$Type<?>> Either<T, DataComponentType<?>> unpackType(final T type) {
      return type instanceof DataComponentPredicate$AnyValueType anyCheck ? Either.right(anyCheck.componentType()) : Either.left(type);
   }

   private static DataComponentPredicate$Type<?> copyOrCreateType(final Either<DataComponentPredicate$Type<?>, DataComponentType<?>> concreteTypeOrComponent) {
      return (DataComponentPredicate$Type<?>)concreteTypeOrComponent.map(concrete -> concrete, DataComponentPredicate$AnyValueType::create);
   }

   Codec<T> codec();

   MapCodec<DataComponentPredicate$Single<T>> wrappedCodec();

   StreamCodec<RegistryFriendlyByteBuf, DataComponentPredicate$Single<T>> singleStreamCodec();
}
