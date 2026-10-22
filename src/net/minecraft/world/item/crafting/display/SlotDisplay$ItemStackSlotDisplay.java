package net.minecraft.world.item.crafting.display;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.stream.Stream;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.ItemStackTemplate;

public record SlotDisplay$ItemStackSlotDisplay(ItemStackTemplate stack) implements SlotDisplay {
   public static final MapCodec<SlotDisplay$ItemStackSlotDisplay> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(ItemStackTemplate.CODEC.fieldOf("item").forGetter(SlotDisplay$ItemStackSlotDisplay::stack)).apply(i, SlotDisplay$ItemStackSlotDisplay::new)
   );
   public static final StreamCodec<RegistryFriendlyByteBuf, SlotDisplay$ItemStackSlotDisplay> STREAM_CODEC = StreamCodec.composite(
      ItemStackTemplate.STREAM_CODEC, SlotDisplay$ItemStackSlotDisplay::stack, SlotDisplay$ItemStackSlotDisplay::new
   );
   public static final SlotDisplay$Type<SlotDisplay$ItemStackSlotDisplay> TYPE = new SlotDisplay$Type<>(MAP_CODEC, STREAM_CODEC);

   @Override
   public SlotDisplay$Type<SlotDisplay$ItemStackSlotDisplay> type() {
      return TYPE;
   }

   @Override
   public <T> Stream<T> resolve(final ContextMap context, final DisplayContentsFactory<T> factory) {
      return factory instanceof DisplayContentsFactory$ForStacks<T> stacks ? Stream.of(stacks.forStack(this.stack.create())) : Stream.empty();
   }

   @Override
   public boolean isEnabled(final FeatureFlagSet enabledFeatures) {
      return this.stack.item().value().isEnabled(enabledFeatures);
   }
}
