package net.minecraft.world.item.crafting.display;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup$RegistryLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;

public record SlotDisplay$WithAnyPotion(SlotDisplay display) implements SlotDisplay {
   public static final MapCodec<SlotDisplay$WithAnyPotion> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(SlotDisplay.CODEC.fieldOf("contents").forGetter(SlotDisplay$WithAnyPotion::display)).apply(i, SlotDisplay$WithAnyPotion::new)
   );
   public static final StreamCodec<RegistryFriendlyByteBuf, SlotDisplay$WithAnyPotion> STREAM_CODEC = StreamCodec.composite(
      SlotDisplay.STREAM_CODEC, SlotDisplay$WithAnyPotion::display, SlotDisplay$WithAnyPotion::new
   );
   public static final SlotDisplay$Type<SlotDisplay$WithAnyPotion> TYPE = new SlotDisplay$Type<>(MAP_CODEC, STREAM_CODEC);

   @Override
   public SlotDisplay$Type<SlotDisplay$WithAnyPotion> type() {
      return TYPE;
   }

   @Override
   public <T> Stream<T> resolve(final ContextMap context, final DisplayContentsFactory<T> factory) {
      if (factory instanceof DisplayContentsFactory$ForStacks<T> stacks) {
         List<ItemStack> displayItems = this.display.resolveForStacks(context);
         Optional<? extends HolderLookup$RegistryLookup<Potion>> potions = Optional.ofNullable(context.get(SlotDisplayContext.REGISTRIES))
            .flatMap(r -> r.lookup(Registries.POTION));
         return potions.stream().flatMap(HolderLookup::listElements).flatMap(potion -> {
            PotionContents potionContents = new PotionContents(potion);
            return displayItems.stream().map(item -> {
               ItemStack itemCopy = item.copy();
               itemCopy.set(DataComponents.POTION_CONTENTS, potionContents);
               return stacks.forStack(itemCopy);
            });
         });
      } else {
         return Stream.empty();
      }
   }
}
