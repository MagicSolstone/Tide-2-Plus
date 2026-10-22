package net.minecraft.world.item.crafting.display;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.flag.FeatureFlagSet;

public record SlotDisplay$Composite(List<SlotDisplay> contents) implements SlotDisplay {
   public static final MapCodec<SlotDisplay$Composite> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(SlotDisplay.CODEC.listOf().fieldOf("contents").forGetter(SlotDisplay$Composite::contents)).apply(i, SlotDisplay$Composite::new)
   );
   public static final StreamCodec<RegistryFriendlyByteBuf, SlotDisplay$Composite> STREAM_CODEC = StreamCodec.composite(
      SlotDisplay.STREAM_CODEC.apply(ByteBufCodecs.list()), SlotDisplay$Composite::contents, SlotDisplay$Composite::new
   );
   public static final SlotDisplay$Type<SlotDisplay$Composite> TYPE = new SlotDisplay$Type<>(MAP_CODEC, STREAM_CODEC);

   @Override
   public SlotDisplay$Type<SlotDisplay$Composite> type() {
      return TYPE;
   }

   @Override
   public <T> Stream<T> resolve(final ContextMap context, final DisplayContentsFactory<T> factory) {
      return this.contents.stream().flatMap(d -> d.resolve(context, factory));
   }

   @Override
   public boolean isEnabled(final FeatureFlagSet enabledFeatures) {
      return this.contents.stream().allMatch(c -> c.isEnabled(enabledFeatures));
   }
}
