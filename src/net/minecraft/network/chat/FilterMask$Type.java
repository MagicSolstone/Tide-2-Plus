package net.minecraft.network.chat;

import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.ByIdMap$OutOfBoundsStrategy;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;

enum FilterMask$Type implements StringRepresentable {
   PASS_THROUGH(0, "pass_through", () -> MapCodec.unit(FilterMask.PASS_THROUGH), () -> StreamCodec.unit(FilterMask.PASS_THROUGH)),
   FULLY_FILTERED(1, "fully_filtered", () -> MapCodec.unit(FilterMask.FULLY_FILTERED), () -> StreamCodec.unit(FilterMask.FULLY_FILTERED)),
   PARTIALLY_FILTERED(
      2,
      "partially_filtered",
      () -> ExtraCodecs.BIT_SET.xmap(FilterMask::new, FilterMask::mask).fieldOf("value"),
      () -> ByteBufCodecs.BIT_SET.map(FilterMask::new, FilterMask::mask)
   );

   private static final IntFunction<FilterMask$Type> ID_MAP = ByIdMap.continuous(t -> t.id, values(), ByIdMap$OutOfBoundsStrategy.ZERO);
   public static final StreamCodec<ByteBuf, FilterMask$Type> STREAM_CODEC = ByteBufCodecs.idMapper(ID_MAP, t -> t.id);
   private final int id;
   private final String serializedName;
   private final Supplier<MapCodec<FilterMask>> codec;
   private final Supplier<StreamCodec<ByteBuf, FilterMask>> streamCodec;

   private FilterMask$Type(
      final int id, final String serializedName, final Supplier<MapCodec<FilterMask>> codec, final Supplier<StreamCodec<ByteBuf, FilterMask>> streamCodec
   ) {
      this.id = id;
      this.serializedName = serializedName;
      this.codec = Suppliers.memoize(codec::get);
      this.streamCodec = Suppliers.memoize(streamCodec::get);
   }

   @Override
   public String getSerializedName() {
      return this.serializedName;
   }

   private MapCodec<FilterMask> codec() {
      return this.codec.get();
   }

   private StreamCodec<ByteBuf, FilterMask> streamCodec() {
      return this.streamCodec.get();
   }
}
