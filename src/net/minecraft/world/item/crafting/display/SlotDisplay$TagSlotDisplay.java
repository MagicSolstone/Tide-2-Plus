package net.minecraft.world.item.crafting.display;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.stream.Stream;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.Item;

public record SlotDisplay$TagSlotDisplay(HolderSet<Item> tag) implements SlotDisplay {
   public static final MapCodec<SlotDisplay$TagSlotDisplay> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(RegistryCodecs.holderSet(Registries.ITEM).fieldOf("tag").forGetter(SlotDisplay$TagSlotDisplay::tag))
         .apply(i, SlotDisplay$TagSlotDisplay::new)
   );
   public static final StreamCodec<RegistryFriendlyByteBuf, SlotDisplay$TagSlotDisplay> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.holderSet(Registries.ITEM), SlotDisplay$TagSlotDisplay::tag, SlotDisplay$TagSlotDisplay::new
   );
   public static final SlotDisplay$Type<SlotDisplay$TagSlotDisplay> TYPE = new SlotDisplay$Type<>(MAP_CODEC, STREAM_CODEC);

   @Override
   public SlotDisplay$Type<SlotDisplay$TagSlotDisplay> type() {
      return TYPE;
   }

   @Override
   public <T> Stream<T> resolve(final ContextMap context, final DisplayContentsFactory<T> factory) {
      return factory instanceof DisplayContentsFactory$ForStacks<T> stacks ? this.tag.stream().map(stacks::forStack) : Stream.empty();
   }
}
