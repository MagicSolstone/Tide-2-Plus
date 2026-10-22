package net.minecraft.world.item.crafting.display;

import com.mojang.serialization.MapCodec;
import java.util.stream.Stream;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.context.ContextMap;

public class SlotDisplay$Empty implements SlotDisplay {
   public static final SlotDisplay$Empty INSTANCE = new SlotDisplay$Empty();
   public static final MapCodec<SlotDisplay$Empty> MAP_CODEC = MapCodec.unit(INSTANCE);
   public static final StreamCodec<RegistryFriendlyByteBuf, SlotDisplay$Empty> STREAM_CODEC = StreamCodec.unit(INSTANCE);
   public static final SlotDisplay$Type<SlotDisplay$Empty> TYPE = new SlotDisplay$Type<>(MAP_CODEC, STREAM_CODEC);

   private SlotDisplay$Empty() {
   }

   @Override
   public SlotDisplay$Type<SlotDisplay$Empty> type() {
      return TYPE;
   }

   @Override
   public String toString() {
      return "<empty>";
   }

   @Override
   public <T> Stream<T> resolve(final ContextMap context, final DisplayContentsFactory<T> factory) {
      return Stream.empty();
   }
}
