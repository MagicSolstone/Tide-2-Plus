package net.minecraft.network.codec;

import com.mojang.datafixers.util.Function10;
import com.mojang.datafixers.util.Function11;
import com.mojang.datafixers.util.Function12;
import com.mojang.datafixers.util.Function13;
import com.mojang.datafixers.util.Function14;
import com.mojang.datafixers.util.Function3;
import com.mojang.datafixers.util.Function4;
import com.mojang.datafixers.util.Function5;
import com.mojang.datafixers.util.Function6;
import com.mojang.datafixers.util.Function7;
import com.mojang.datafixers.util.Function8;
import com.mojang.datafixers.util.Function9;
import io.netty.buffer.ByteBuf;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public interface StreamCodec<B, V> extends StreamEncoder<B, V>, StreamDecoder<B, V> {
   static <B, V> StreamCodec<B, V> of(final StreamEncoder<B, V> encoder, final StreamDecoder<B, V> decoder) {
      return new StreamCodec$1(decoder, encoder);
   }

   static <B, V> StreamCodec<B, V> ofMember(final StreamMemberEncoder<B, V> encoder, final StreamDecoder<B, V> decoder) {
      return new StreamCodec$2(decoder, encoder);
   }

   static <B, V> StreamCodec<B, V> unit(final V instance) {
      return new StreamCodec$3(instance);
   }

   default <O> StreamCodec<B, O> apply(final StreamCodec$CodecOperation<B, V, O> operation) {
      return operation.apply(this);
   }

   default <O> StreamCodec<B, O> map(final Function<? super V, ? extends O> to, final Function<? super O, ? extends V> from) {
      return new StreamCodec$4(this, to, from);
   }

   default <O extends ByteBuf> StreamCodec<O, V> mapStream(final Function<O, ? extends B> operation) {
      return new StreamCodec$5(this, operation);
   }

   default <U> StreamCodec<B, U> dispatch(
      final Function<? super U, ? extends V> type, final Function<? super V, ? extends StreamCodec<? super B, ? extends U>> codec
   ) {
      return new StreamCodec$6(this, codec, type);
   }

   static <B, C, T1> StreamCodec<B, C> composite(final StreamCodec<? super B, T1> codec1, final Function<C, T1> getter1, final Function<T1, C> constructor) {
      return new StreamCodec$7(codec1, constructor, getter1);
   }

   static <B, C, T1, T2> StreamCodec<B, C> composite(
      final StreamCodec<? super B, T1> codec1,
      final Function<C, T1> getter1,
      final StreamCodec<? super B, T2> codec2,
      final Function<C, T2> getter2,
      final BiFunction<T1, T2, C> constructor
   ) {
      return new StreamCodec$8(codec1, codec2, constructor, getter1, getter2);
   }

   static <B, C, T1, T2, T3> StreamCodec<B, C> composite(
      final StreamCodec<? super B, T1> codec1,
      final Function<C, T1> getter1,
      final StreamCodec<? super B, T2> codec2,
      final Function<C, T2> getter2,
      final StreamCodec<? super B, T3> codec3,
      final Function<C, T3> getter3,
      final Function3<T1, T2, T3, C> constructor
   ) {
      return new StreamCodec$9(codec1, codec2, codec3, constructor, getter1, getter2, getter3);
   }

   static <B, C, T1, T2, T3, T4> StreamCodec<B, C> composite(
      final StreamCodec<? super B, T1> codec1,
      final Function<C, T1> getter1,
      final StreamCodec<? super B, T2> codec2,
      final Function<C, T2> getter2,
      final StreamCodec<? super B, T3> codec3,
      final Function<C, T3> getter3,
      final StreamCodec<? super B, T4> codec4,
      final Function<C, T4> getter4,
      final Function4<T1, T2, T3, T4, C> constructor
   ) {
      return new StreamCodec$10(codec1, codec2, codec3, codec4, constructor, getter1, getter2, getter3, getter4);
   }

   static <B, C, T1, T2, T3, T4, T5> StreamCodec<B, C> composite(
      final StreamCodec<? super B, T1> codec1,
      final Function<C, T1> getter1,
      final StreamCodec<? super B, T2> codec2,
      final Function<C, T2> getter2,
      final StreamCodec<? super B, T3> codec3,
      final Function<C, T3> getter3,
      final StreamCodec<? super B, T4> codec4,
      final Function<C, T4> getter4,
      final StreamCodec<? super B, T5> codec5,
      final Function<C, T5> getter5,
      final Function5<T1, T2, T3, T4, T5, C> constructor
   ) {
      return new StreamCodec$11(codec1, codec2, codec3, codec4, codec5, constructor, getter1, getter2, getter3, getter4, getter5);
   }

   static <B, C, T1, T2, T3, T4, T5, T6> StreamCodec<B, C> composite(
      final StreamCodec<? super B, T1> codec1,
      final Function<C, T1> getter1,
      final StreamCodec<? super B, T2> codec2,
      final Function<C, T2> getter2,
      final StreamCodec<? super B, T3> codec3,
      final Function<C, T3> getter3,
      final StreamCodec<? super B, T4> codec4,
      final Function<C, T4> getter4,
      final StreamCodec<? super B, T5> codec5,
      final Function<C, T5> getter5,
      final StreamCodec<? super B, T6> codec6,
      final Function<C, T6> getter6,
      final Function6<T1, T2, T3, T4, T5, T6, C> constructor
   ) {
      return new StreamCodec$12(codec1, codec2, codec3, codec4, codec5, codec6, constructor, getter1, getter2, getter3, getter4, getter5, getter6);
   }

   static <B, C, T1, T2, T3, T4, T5, T6, T7> StreamCodec<B, C> composite(
      final StreamCodec<? super B, T1> codec1,
      final Function<C, T1> getter1,
      final StreamCodec<? super B, T2> codec2,
      final Function<C, T2> getter2,
      final StreamCodec<? super B, T3> codec3,
      final Function<C, T3> getter3,
      final StreamCodec<? super B, T4> codec4,
      final Function<C, T4> getter4,
      final StreamCodec<? super B, T5> codec5,
      final Function<C, T5> getter5,
      final StreamCodec<? super B, T6> codec6,
      final Function<C, T6> getter6,
      final StreamCodec<? super B, T7> codec7,
      final Function<C, T7> getter7,
      final Function7<T1, T2, T3, T4, T5, T6, T7, C> constructor
   ) {
      return new StreamCodec$13(
         codec1, codec2, codec3, codec4, codec5, codec6, codec7, constructor, getter1, getter2, getter3, getter4, getter5, getter6, getter7
      );
   }

   static <B, C, T1, T2, T3, T4, T5, T6, T7, T8> StreamCodec<B, C> composite(
      final StreamCodec<? super B, T1> codec1,
      final Function<C, T1> getter1,
      final StreamCodec<? super B, T2> codec2,
      final Function<C, T2> getter2,
      final StreamCodec<? super B, T3> codec3,
      final Function<C, T3> getter3,
      final StreamCodec<? super B, T4> codec4,
      final Function<C, T4> getter4,
      final StreamCodec<? super B, T5> codec5,
      final Function<C, T5> getter5,
      final StreamCodec<? super B, T6> codec6,
      final Function<C, T6> getter6,
      final StreamCodec<? super B, T7> codec7,
      final Function<C, T7> getter7,
      final StreamCodec<? super B, T8> codec8,
      final Function<C, T8> getter8,
      final Function8<T1, T2, T3, T4, T5, T6, T7, T8, C> constructor
   ) {
      return new StreamCodec$14(
         codec1, codec2, codec3, codec4, codec5, codec6, codec7, codec8, constructor, getter1, getter2, getter3, getter4, getter5, getter6, getter7, getter8
      );
   }

   static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9> StreamCodec<B, C> composite(
      final StreamCodec<? super B, T1> codec1,
      final Function<C, T1> getter1,
      final StreamCodec<? super B, T2> codec2,
      final Function<C, T2> getter2,
      final StreamCodec<? super B, T3> codec3,
      final Function<C, T3> getter3,
      final StreamCodec<? super B, T4> codec4,
      final Function<C, T4> getter4,
      final StreamCodec<? super B, T5> codec5,
      final Function<C, T5> getter5,
      final StreamCodec<? super B, T6> codec6,
      final Function<C, T6> getter6,
      final StreamCodec<? super B, T7> codec7,
      final Function<C, T7> getter7,
      final StreamCodec<? super B, T8> codec8,
      final Function<C, T8> getter8,
      final StreamCodec<? super B, T9> codec9,
      final Function<C, T9> getter9,
      final Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, C> constructor
   ) {
      return new StreamCodec$15(
         codec1,
         codec2,
         codec3,
         codec4,
         codec5,
         codec6,
         codec7,
         codec8,
         codec9,
         constructor,
         getter1,
         getter2,
         getter3,
         getter4,
         getter5,
         getter6,
         getter7,
         getter8,
         getter9
      );
   }

   static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> StreamCodec<B, C> composite(
      final StreamCodec<? super B, T1> codec1,
      final Function<C, T1> getter1,
      final StreamCodec<? super B, T2> codec2,
      final Function<C, T2> getter2,
      final StreamCodec<? super B, T3> codec3,
      final Function<C, T3> getter3,
      final StreamCodec<? super B, T4> codec4,
      final Function<C, T4> getter4,
      final StreamCodec<? super B, T5> codec5,
      final Function<C, T5> getter5,
      final StreamCodec<? super B, T6> codec6,
      final Function<C, T6> getter6,
      final StreamCodec<? super B, T7> codec7,
      final Function<C, T7> getter7,
      final StreamCodec<? super B, T8> codec8,
      final Function<C, T8> getter8,
      final StreamCodec<? super B, T9> codec9,
      final Function<C, T9> getter9,
      final StreamCodec<? super B, T10> codec10,
      final Function<C, T10> getter10,
      final Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, C> constructor
   ) {
      return new StreamCodec$16(
         codec1,
         codec2,
         codec3,
         codec4,
         codec5,
         codec6,
         codec7,
         codec8,
         codec9,
         codec10,
         constructor,
         getter1,
         getter2,
         getter3,
         getter4,
         getter5,
         getter6,
         getter7,
         getter8,
         getter9,
         getter10
      );
   }

   static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> StreamCodec<B, C> composite(
      final StreamCodec<? super B, T1> codec1,
      final Function<C, T1> getter1,
      final StreamCodec<? super B, T2> codec2,
      final Function<C, T2> getter2,
      final StreamCodec<? super B, T3> codec3,
      final Function<C, T3> getter3,
      final StreamCodec<? super B, T4> codec4,
      final Function<C, T4> getter4,
      final StreamCodec<? super B, T5> codec5,
      final Function<C, T5> getter5,
      final StreamCodec<? super B, T6> codec6,
      final Function<C, T6> getter6,
      final StreamCodec<? super B, T7> codec7,
      final Function<C, T7> getter7,
      final StreamCodec<? super B, T8> codec8,
      final Function<C, T8> getter8,
      final StreamCodec<? super B, T9> codec9,
      final Function<C, T9> getter9,
      final StreamCodec<? super B, T10> codec10,
      final Function<C, T10> getter10,
      final StreamCodec<? super B, T11> codec11,
      final Function<C, T11> getter11,
      final Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, C> constructor
   ) {
      return new StreamCodec$17(
         codec1,
         codec2,
         codec3,
         codec4,
         codec5,
         codec6,
         codec7,
         codec8,
         codec9,
         codec10,
         codec11,
         constructor,
         getter1,
         getter2,
         getter3,
         getter4,
         getter5,
         getter6,
         getter7,
         getter8,
         getter9,
         getter10,
         getter11
      );
   }

   static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> StreamCodec<B, C> composite(
      final StreamCodec<? super B, T1> codec1,
      final Function<C, T1> getter1,
      final StreamCodec<? super B, T2> codec2,
      final Function<C, T2> getter2,
      final StreamCodec<? super B, T3> codec3,
      final Function<C, T3> getter3,
      final StreamCodec<? super B, T4> codec4,
      final Function<C, T4> getter4,
      final StreamCodec<? super B, T5> codec5,
      final Function<C, T5> getter5,
      final StreamCodec<? super B, T6> codec6,
      final Function<C, T6> getter6,
      final StreamCodec<? super B, T7> codec7,
      final Function<C, T7> getter7,
      final StreamCodec<? super B, T8> codec8,
      final Function<C, T8> getter8,
      final StreamCodec<? super B, T9> codec9,
      final Function<C, T9> getter9,
      final StreamCodec<? super B, T10> codec10,
      final Function<C, T10> getter10,
      final StreamCodec<? super B, T11> codec11,
      final Function<C, T11> getter11,
      final StreamCodec<? super B, T12> codec12,
      final Function<C, T12> getter12,
      final Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, C> constructor
   ) {
      return new StreamCodec$18(
         codec1,
         codec2,
         codec3,
         codec4,
         codec5,
         codec6,
         codec7,
         codec8,
         codec9,
         codec10,
         codec11,
         codec12,
         constructor,
         getter1,
         getter2,
         getter3,
         getter4,
         getter5,
         getter6,
         getter7,
         getter8,
         getter9,
         getter10,
         getter11,
         getter12
      );
   }

   static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> StreamCodec<B, C> composite(
      final StreamCodec<? super B, T1> codec1,
      final Function<C, T1> getter1,
      final StreamCodec<? super B, T2> codec2,
      final Function<C, T2> getter2,
      final StreamCodec<? super B, T3> codec3,
      final Function<C, T3> getter3,
      final StreamCodec<? super B, T4> codec4,
      final Function<C, T4> getter4,
      final StreamCodec<? super B, T5> codec5,
      final Function<C, T5> getter5,
      final StreamCodec<? super B, T6> codec6,
      final Function<C, T6> getter6,
      final StreamCodec<? super B, T7> codec7,
      final Function<C, T7> getter7,
      final StreamCodec<? super B, T8> codec8,
      final Function<C, T8> getter8,
      final StreamCodec<? super B, T9> codec9,
      final Function<C, T9> getter9,
      final StreamCodec<? super B, T10> codec10,
      final Function<C, T10> getter10,
      final StreamCodec<? super B, T11> codec11,
      final Function<C, T11> getter11,
      final StreamCodec<? super B, T12> codec12,
      final Function<C, T12> getter12,
      final StreamCodec<? super B, T13> codec13,
      final Function<C, T13> getter13,
      final Function13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, C> constructor
   ) {
      return new StreamCodec$19(
         codec1,
         codec2,
         codec3,
         codec4,
         codec5,
         codec6,
         codec7,
         codec8,
         codec9,
         codec10,
         codec11,
         codec12,
         codec13,
         constructor,
         getter1,
         getter2,
         getter3,
         getter4,
         getter5,
         getter6,
         getter7,
         getter8,
         getter9,
         getter10,
         getter11,
         getter12,
         getter13
      );
   }

   static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> StreamCodec<B, C> composite(
      final StreamCodec<? super B, T1> codec1,
      final Function<C, T1> getter1,
      final StreamCodec<? super B, T2> codec2,
      final Function<C, T2> getter2,
      final StreamCodec<? super B, T3> codec3,
      final Function<C, T3> getter3,
      final StreamCodec<? super B, T4> codec4,
      final Function<C, T4> getter4,
      final StreamCodec<? super B, T5> codec5,
      final Function<C, T5> getter5,
      final StreamCodec<? super B, T6> codec6,
      final Function<C, T6> getter6,
      final StreamCodec<? super B, T7> codec7,
      final Function<C, T7> getter7,
      final StreamCodec<? super B, T8> codec8,
      final Function<C, T8> getter8,
      final StreamCodec<? super B, T9> codec9,
      final Function<C, T9> getter9,
      final StreamCodec<? super B, T10> codec10,
      final Function<C, T10> getter10,
      final StreamCodec<? super B, T11> codec11,
      final Function<C, T11> getter11,
      final StreamCodec<? super B, T12> codec12,
      final Function<C, T12> getter12,
      final StreamCodec<? super B, T13> codec13,
      final Function<C, T13> getter13,
      final StreamCodec<? super B, T14> codec14,
      final Function<C, T14> getter14,
      final Function14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, C> constructor
   ) {
      return new StreamCodec$20(
         codec1,
         codec2,
         codec3,
         codec4,
         codec5,
         codec6,
         codec7,
         codec8,
         codec9,
         codec10,
         codec11,
         codec12,
         codec13,
         codec14,
         constructor,
         getter1,
         getter2,
         getter3,
         getter4,
         getter5,
         getter6,
         getter7,
         getter8,
         getter9,
         getter10,
         getter11,
         getter12,
         getter13,
         getter14
      );
   }

   static <B, T> StreamCodec<B, T> recursive(final UnaryOperator<StreamCodec<B, T>> factory) {
      return new StreamCodec$21(factory);
   }

   default <S extends B> StreamCodec<S, V> cast() {
      return this;
   }
}
