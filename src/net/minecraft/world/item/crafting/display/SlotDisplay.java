package net.minecraft.world.item.crafting.display;

import com.mojang.serialization.Codec;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.ItemStack;

public interface SlotDisplay {
   Codec<SlotDisplay> CODEC = BuiltInRegistries.SLOT_DISPLAY.byNameCodec().dispatch(SlotDisplay::type, SlotDisplay$Type::codec);
   StreamCodec<RegistryFriendlyByteBuf, SlotDisplay> STREAM_CODEC = ByteBufCodecs.registry(Registries.SLOT_DISPLAY)
      .dispatch(SlotDisplay::type, SlotDisplay$Type::streamCodec);

   <T> Stream<T> resolve(ContextMap context, DisplayContentsFactory<T> builder);

   SlotDisplay$Type<? extends SlotDisplay> type();

   default boolean isEnabled(final FeatureFlagSet enabledFeatures) {
      return true;
   }

   default List<ItemStack> resolveForStacks(final ContextMap context) {
      return this.resolve(context, SlotDisplay$ItemStackContentsFactory.INSTANCE).toList();
   }

   default ItemStack resolveForFirstStack(final ContextMap context) {
      return this.resolve(context, SlotDisplay$ItemStackContentsFactory.INSTANCE).findFirst().orElse(ItemStack.EMPTY);
   }

   private static <T> Stream<T> applyDemoTransformation(
      final ContextMap context,
      final DisplayContentsFactory<T> factory,
      final SlotDisplay firstDisplay,
      final SlotDisplay secondDisplay,
      final RandomSource randomSource,
      final BinaryOperator<ItemStack> operation
   ) {
      if (factory instanceof DisplayContentsFactory$ForStacks<T> stacks) {
         List<ItemStack> firstItems = firstDisplay.resolveForStacks(context);
         if (firstItems.isEmpty()) {
            return Stream.empty();
         } else {
            List<ItemStack> secondItems = secondDisplay.resolveForStacks(context);
            return secondItems.isEmpty() ? Stream.empty() : Stream.<ItemStack>generate(() -> {
               ItemStack first = Util.getRandom(firstItems, randomSource);
               ItemStack second = Util.getRandom(secondItems, randomSource);
               return operation.apply(first, second);
            }).limit(256L).filter(s -> !s.isEmpty()).limit(16L).map(stacks::forStack);
         }
      } else {
         return Stream.empty();
      }
   }

   private static <T> Stream<T> applyDemoTransformation(
      final ContextMap context,
      final DisplayContentsFactory<T> factory,
      final SlotDisplay firstDisplay,
      final SlotDisplay secondDisplay,
      final BinaryOperator<ItemStack> operation
   ) {
      if (factory instanceof DisplayContentsFactory$ForStacks<T> stacks) {
         List<ItemStack> firstItems = firstDisplay.resolveForStacks(context);
         if (firstItems.isEmpty()) {
            return Stream.empty();
         } else {
            List<ItemStack> secondItems = secondDisplay.resolveForStacks(context);
            if (secondItems.isEmpty()) {
               return Stream.empty();
            } else {
               int cycle = firstItems.size() * secondItems.size();
               return IntStream.range(0, cycle).mapToObj(index -> {
                  int firstItemCount = firstItems.size();
                  int firstItemIndex = index % firstItemCount;
                  int secondItemIndex = index / firstItemCount;
                  ItemStack first = firstItems.get(firstItemIndex);
                  ItemStack second = secondItems.get(secondItemIndex);
                  return operation.apply(first, second);
               }).filter(s -> !s.isEmpty()).limit(16L).map(stacks::forStack);
            }
         }
      } else {
         return Stream.empty();
      }
   }
}
