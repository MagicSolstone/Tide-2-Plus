package net.minecraft.world.item.crafting.display;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;

public record SlotDisplay$ItemSlotDisplay(Holder<Item> item) implements SlotDisplay {
   public static final MapCodec<SlotDisplay$ItemSlotDisplay> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(Item.CODEC.fieldOf("item").forGetter(SlotDisplay$ItemSlotDisplay::item)).apply(i, SlotDisplay$ItemSlotDisplay::new)
   );
   public static final StreamCodec<RegistryFriendlyByteBuf, SlotDisplay$ItemSlotDisplay> STREAM_CODEC = StreamCodec.composite(
      Item.STREAM_CODEC, SlotDisplay$ItemSlotDisplay::item, SlotDisplay$ItemSlotDisplay::new
   );
   public static final SlotDisplay$Type<SlotDisplay$ItemSlotDisplay> TYPE = new SlotDisplay$Type<>(MAP_CODEC, STREAM_CODEC);

   public SlotDisplay$ItemSlotDisplay(final Item item) {
      this(item.builtInRegistryHolder());
   }

   @Override
   public SlotDisplay$Type<SlotDisplay$ItemSlotDisplay> type() {
      return TYPE;
   }

   @Override
   public <T> Stream<T> resolve(final ContextMap context, final DisplayContentsFactory<T> factory) {
      return factory instanceof DisplayContentsFactory$ForStacks<T> stacks ? Stream.of(stacks.forStack(this.item)) : Stream.empty();
   }

   @Override
   public boolean isEnabled(final FeatureFlagSet enabledFeatures) {
      return this.item.value().isEnabled(enabledFeatures);
   }
}
