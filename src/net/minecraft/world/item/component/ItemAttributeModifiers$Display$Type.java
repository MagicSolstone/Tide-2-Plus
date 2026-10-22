package net.minecraft.world.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import java.util.function.IntFunction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.ByIdMap$OutOfBoundsStrategy;
import net.minecraft.util.StringRepresentable;

public enum ItemAttributeModifiers$Display$Type implements StringRepresentable {
   DEFAULT("default", 0, ItemAttributeModifiers$Display$Default.CODEC, ItemAttributeModifiers$Display$Default.STREAM_CODEC),
   HIDDEN("hidden", 1, ItemAttributeModifiers$Display$Hidden.CODEC, ItemAttributeModifiers$Display$Hidden.STREAM_CODEC),
   OVERRIDE("override", 2, ItemAttributeModifiers$Display$OverrideText.CODEC, ItemAttributeModifiers$Display$OverrideText.STREAM_CODEC);

   private static final Codec<ItemAttributeModifiers$Display$Type> CODEC = StringRepresentable.fromEnum(ItemAttributeModifiers$Display$Type::values);
   private static final IntFunction<ItemAttributeModifiers$Display$Type> BY_ID = ByIdMap.continuous(
      ItemAttributeModifiers$Display$Type::id, values(), ByIdMap$OutOfBoundsStrategy.ZERO
   );
   private static final StreamCodec<ByteBuf, ItemAttributeModifiers$Display$Type> STREAM_CODEC = ByteBufCodecs.idMapper(
      BY_ID, ItemAttributeModifiers$Display$Type::id
   );
   private final String name;
   private final int id;
   private final MapCodec<? extends ItemAttributeModifiers$Display> codec;
   private final StreamCodec<RegistryFriendlyByteBuf, ? extends ItemAttributeModifiers$Display> streamCodec;

   private ItemAttributeModifiers$Display$Type(
      final String name,
      final int id,
      final MapCodec<? extends ItemAttributeModifiers$Display> codec,
      final StreamCodec<RegistryFriendlyByteBuf, ? extends ItemAttributeModifiers$Display> streamCodec
   ) {
      this.name = name;
      this.id = id;
      this.codec = codec;
      this.streamCodec = streamCodec;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }

   private int id() {
      return this.id;
   }

   private StreamCodec<RegistryFriendlyByteBuf, ? extends ItemAttributeModifiers$Display> streamCodec() {
      return this.streamCodec;
   }
}
