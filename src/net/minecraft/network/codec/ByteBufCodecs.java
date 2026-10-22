package net.minecraft.network.codec;

import com.google.gson.JsonElement;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import java.security.Key;
import java.security.PublicKey;
import java.time.Instant;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.IdMap;
import net.minecraft.core.Registry;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.VarInt;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Crypt;
import net.minecraft.util.CryptException;
import net.minecraft.util.Mth;
import org.joml.Quaternionfc;
import org.joml.Vector3fc;

public interface ByteBufCodecs {
   int MAX_INITIAL_COLLECTION_SIZE = 65536;
   StreamCodec<ByteBuf, Boolean> BOOL = new ByteBufCodecs$1();
   StreamCodec<ByteBuf, Byte> BYTE = new ByteBufCodecs$2();
   StreamCodec<ByteBuf, Float> ROTATION_BYTE = BYTE.map(Mth::unpackDegrees, Mth::packDegrees);
   StreamCodec<ByteBuf, Short> SHORT = new ByteBufCodecs$3();
   StreamCodec<ByteBuf, Integer> UNSIGNED_SHORT = new ByteBufCodecs$4();
   StreamCodec<ByteBuf, Integer> INT = new ByteBufCodecs$5();
   StreamCodec<ByteBuf, Integer> VAR_INT = new ByteBufCodecs$6();
   StreamCodec<ByteBuf, OptionalInt> OPTIONAL_VAR_INT = VAR_INT.map(
      i -> i == 0 ? OptionalInt.empty() : OptionalInt.of(i - 1), o -> o.isPresent() ? o.getAsInt() + 1 : 0
   );
   StreamCodec<ByteBuf, Long> LONG = new ByteBufCodecs$7();
   StreamCodec<ByteBuf, Long> VAR_LONG = new ByteBufCodecs$8();
   StreamCodec<ByteBuf, Float> FLOAT = new ByteBufCodecs$9();
   StreamCodec<ByteBuf, Double> DOUBLE = new ByteBufCodecs$10();
   StreamCodec<ByteBuf, byte[]> BYTE_ARRAY = new ByteBufCodecs$12();
   StreamCodec<ByteBuf, long[]> LONG_ARRAY = new ByteBufCodecs$13();
   StreamCodec<ByteBuf, BitSet> BIT_SET = new ByteBufCodecs$15();
   StreamCodec<ByteBuf, String> STRING_UTF8 = stringUtf8(32767);
   StreamCodec<ByteBuf, Tag> TAG = tagCodec(NbtAccounter::defaultQuota);
   StreamCodec<ByteBuf, Tag> TRUSTED_TAG = tagCodec(NbtAccounter::unlimitedHeap);
   StreamCodec<ByteBuf, CompoundTag> COMPOUND_TAG = compoundTagCodec(NbtAccounter::defaultQuota);
   StreamCodec<ByteBuf, CompoundTag> TRUSTED_COMPOUND_TAG = compoundTagCodec(NbtAccounter::unlimitedHeap);
   StreamCodec<ByteBuf, Optional<CompoundTag>> OPTIONAL_COMPOUND_TAG = new ByteBufCodecs$21();
   StreamCodec<ByteBuf, Vector3fc> VECTOR3F = new ByteBufCodecs$22();
   StreamCodec<ByteBuf, Quaternionfc> QUATERNIONF = new ByteBufCodecs$23();
   StreamCodec<ByteBuf, Integer> CONTAINER_ID = new ByteBufCodecs$24();
   StreamCodec<ByteBuf, PropertyMap> GAME_PROFILE_PROPERTIES = new ByteBufCodecs$35();
   StreamCodec<ByteBuf, String> PLAYER_NAME = stringUtf8(16);
   StreamCodec<ByteBuf, GameProfile> GAME_PROFILE = StreamCodec.composite(
      UUIDUtil.STREAM_CODEC, GameProfile::id, PLAYER_NAME, GameProfile::name, GAME_PROFILE_PROPERTIES, GameProfile::properties, GameProfile::new
   );
   StreamCodec<ByteBuf, Integer> RGB_COLOR = new ByteBufCodecs$36();
   StreamCodec<ByteBuf, Instant> INSTANT = LONG.map(Instant::ofEpochMilli, Instant::toEpochMilli);
   int PUBLIC_KEY_SIZE = 256;
   int MAX_PUBLIC_KEY_HEADER_SIZE = 256;
   int MAX_PUBLIC_KEY_LENGTH = 512;
   StreamCodec<ByteBuf, PublicKey> PUBLIC_KEY = byteArray(512).map(bytes -> {
      try {
         return Crypt.byteToPublicKey(bytes);
      } catch (CryptException var2) {
         throw new DecoderException("Malformed public key bytes", var2);
      }
   }, Key::getEncoded);

   static StreamCodec<ByteBuf, byte[]> byteArray(final int maxSize) {
      return new ByteBufCodecs$11(maxSize);
   }

   static StreamCodec<ByteBuf, String> stringUtf8(final int maxStringLength) {
      return new ByteBufCodecs$14(maxStringLength);
   }

   static StreamCodec<ByteBuf, BitSet> fixedBitSet(final int size) {
      return new ByteBufCodecs$16(size);
   }

   static StreamCodec<ByteBuf, Optional<Tag>> optionalTagCodec(final Supplier<NbtAccounter> accounter) {
      return new ByteBufCodecs$17(accounter);
   }

   static StreamCodec<ByteBuf, Tag> tagCodec(final Supplier<NbtAccounter> accounter) {
      return new ByteBufCodecs$18(accounter);
   }

   static StreamCodec<ByteBuf, CompoundTag> compoundTagCodec(final Supplier<NbtAccounter> accounter) {
      return tagCodec(accounter).map(tag -> {
         if (tag instanceof CompoundTag compoundTag) {
            return compoundTag;
         } else {
            throw new DecoderException("Not a compound tag: " + tag);
         }
      }, compoundTag -> (Tag)compoundTag);
   }

   static <T> StreamCodec<ByteBuf, T> fromCodecTrusted(final Codec<T> codec) {
      return fromCodec(codec, NbtAccounter::unlimitedHeap);
   }

   static <T> StreamCodec<ByteBuf, T> fromCodec(final Codec<T> codec) {
      return fromCodec(codec, NbtAccounter::defaultQuota);
   }

   static <T, B extends ByteBuf, V> StreamCodec$CodecOperation<B, T, V> fromCodec(final DynamicOps<T> ops, final Codec<V> codec) {
      return original -> new ByteBufCodecs$19(original, codec, ops);
   }

   static <T> StreamCodec<ByteBuf, T> fromCodec(final Codec<T> codec, final Supplier<NbtAccounter> accounter) {
      return tagCodec(accounter).apply(fromCodec(NbtOps.INSTANCE, codec));
   }

   static <T> StreamCodec<RegistryFriendlyByteBuf, T> fromCodecWithRegistriesTrusted(final Codec<T> codec) {
      return fromCodecWithRegistries(codec, NbtAccounter::unlimitedHeap);
   }

   static <T> StreamCodec<RegistryFriendlyByteBuf, T> fromCodecWithRegistries(final Codec<T> codec) {
      return fromCodecWithRegistries(codec, NbtAccounter::defaultQuota);
   }

   static <T> StreamCodec<RegistryFriendlyByteBuf, T> fromCodecWithRegistries(final Codec<T> codec, final Supplier<NbtAccounter> accounter) {
      StreamCodec<ByteBuf, Tag> tagCodec = tagCodec(accounter);
      return new ByteBufCodecs$20(tagCodec, codec);
   }

   static <B extends ByteBuf, V> StreamCodec<B, Optional<V>> optional(final StreamCodec<? super B, V> original) {
      return new ByteBufCodecs$25(original);
   }

   static int readCount(final ByteBuf input, final int maxSize) {
      int count = VarInt.read(input);
      if (count > maxSize) {
         throw new DecoderException(count + " elements exceeded max size of: " + maxSize);
      } else {
         return count;
      }
   }

   static void writeCount(final ByteBuf output, final int count, final int maxSize) {
      if (count > maxSize) {
         throw new EncoderException(count + " elements exceeded max size of: " + maxSize);
      } else {
         VarInt.write(output, count);
      }
   }

   static <B extends ByteBuf, V, C extends Collection<V>> StreamCodec<B, C> collection(
      final IntFunction<C> constructor, final StreamCodec<? super B, V> elementCodec
   ) {
      return collection(constructor, elementCodec, Integer.MAX_VALUE);
   }

   static <B extends ByteBuf, V, C extends Collection<V>> StreamCodec<B, C> collection(
      final IntFunction<C> constructor, final StreamCodec<? super B, V> elementCodec, final int maxSize
   ) {
      return new ByteBufCodecs$26(maxSize, constructor, elementCodec);
   }

   static <B extends ByteBuf, V, C extends Collection<V>> StreamCodec$CodecOperation<B, V, C> collection(final IntFunction<C> constructor) {
      return original -> collection(constructor, original);
   }

   static <B extends ByteBuf, V, C extends Collection<V>> StreamCodec$CodecOperation<B, V, C> collection(final IntFunction<C> constructor, final int maxSize) {
      return original -> collection(constructor, original, maxSize);
   }

   static <B extends ByteBuf, V> StreamCodec$CodecOperation<B, V, List<V>> list() {
      return original -> collection(ArrayList::new, original);
   }

   static <B extends ByteBuf, V> StreamCodec$CodecOperation<B, V, List<V>> list(final int maxSize) {
      return original -> collection(ArrayList::new, original, maxSize);
   }

   static <B extends ByteBuf, V, C extends Collection<V>> StreamCodec<B, C> fixedSizeCollection(
      final IntFunction<C> constructor, final StreamCodec<? super B, V> elementCodec, final int size
   ) {
      return new ByteBufCodecs$27(constructor, size, elementCodec);
   }

   static <B extends ByteBuf, V, C extends Collection<V>> StreamCodec$CodecOperation<B, V, C> fixedSizeCollection(
      final IntFunction<C> constructor, final int size
   ) {
      return original -> fixedSizeCollection(constructor, original, size);
   }

   static <B extends ByteBuf, V> StreamCodec$CodecOperation<B, V, List<V>> fixedSizeList(final int size) {
      return original -> fixedSizeCollection(ArrayList::new, original, size);
   }

   static <B extends ByteBuf, K, V, M extends Map<K, V>> StreamCodec<B, M> map(
      final IntFunction<? extends M> constructor, final StreamCodec<? super B, K> keyCodec, final StreamCodec<? super B, V> valueCodec
   ) {
      return map(constructor, keyCodec, valueCodec, Integer.MAX_VALUE);
   }

   static <B extends ByteBuf, K, V, M extends Map<K, V>> StreamCodec<B, M> map(
      final IntFunction<? extends M> constructor, final StreamCodec<? super B, K> keyCodec, final StreamCodec<? super B, V> valueCodec, final int maxSize
   ) {
      return new ByteBufCodecs$28(maxSize, keyCodec, valueCodec, constructor);
   }

   static <B extends ByteBuf, L, R> StreamCodec<B, Either<L, R>> either(final StreamCodec<? super B, L> leftCodec, final StreamCodec<? super B, R> rightCodec) {
      return new ByteBufCodecs$29(leftCodec, rightCodec);
   }

   static <B extends ByteBuf, V> StreamCodec$CodecOperation<B, V, V> lengthPrefixed(final int maxSize, final BiFunction<B, ByteBuf, B> decorator) {
      return original -> new ByteBufCodecs$30(maxSize, decorator, original);
   }

   static <V> StreamCodec$CodecOperation<ByteBuf, V, V> lengthPrefixed(final int maxSize) {
      return lengthPrefixed(maxSize, (parent, child) -> child);
   }

   static <V> StreamCodec$CodecOperation<RegistryFriendlyByteBuf, V, V> registryFriendlyLengthPrefixed(final int maxSize) {
      return lengthPrefixed(maxSize, (parent, child) -> new RegistryFriendlyByteBuf(child, parent.registryAccess()));
   }

   static <T> StreamCodec<ByteBuf, T> idMapper(final IntFunction<T> byId, final ToIntFunction<T> toId) {
      return new ByteBufCodecs$31(byId, toId);
   }

   static <T> StreamCodec<ByteBuf, T> idMapper(final IdMap<T> mapper) {
      return idMapper(mapper::byIdOrThrow, mapper::getIdOrThrow);
   }

   private static <T, R> StreamCodec<RegistryFriendlyByteBuf, R> registry(
      final ResourceKey<? extends Registry<T>> registryKey, final Function<Registry<T>, IdMap<R>> mapExtractor
   ) {
      return new ByteBufCodecs$32(mapExtractor, registryKey);
   }

   static <T> StreamCodec<RegistryFriendlyByteBuf, T> registry(final ResourceKey<? extends Registry<T>> registryKey) {
      return registry(registryKey, r -> r);
   }

   static <T> StreamCodec<RegistryFriendlyByteBuf, Holder<T>> holderRegistry(final ResourceKey<? extends Registry<T>> registryKey) {
      return registry(registryKey, Registry::asHolderIdMap);
   }

   static <T> StreamCodec<RegistryFriendlyByteBuf, Holder<T>> holder(
      final ResourceKey<? extends Registry<T>> registryKey, final StreamCodec<? super RegistryFriendlyByteBuf, T> directCodec
   ) {
      return new ByteBufCodecs$33(registryKey, directCodec);
   }

   static <T> StreamCodec<RegistryFriendlyByteBuf, HolderSet<T>> holderSet(final ResourceKey<? extends Registry<T>> registryKey) {
      return new ByteBufCodecs$34(registryKey);
   }

   static StreamCodec<ByteBuf, JsonElement> lenientJson(final int maxStringLength) {
      return new ByteBufCodecs$37(maxStringLength);
   }
}
