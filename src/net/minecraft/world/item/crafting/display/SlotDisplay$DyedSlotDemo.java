package net.minecraft.world.item.crafting.display;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;

public record SlotDisplay$DyedSlotDemo(SlotDisplay dye, SlotDisplay target) implements SlotDisplay {
   public static final MapCodec<SlotDisplay$DyedSlotDemo> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            SlotDisplay.CODEC.fieldOf("dye").forGetter(SlotDisplay$DyedSlotDemo::dye),
            SlotDisplay.CODEC.fieldOf("target").forGetter(SlotDisplay$DyedSlotDemo::target)
         )
         .apply(i, SlotDisplay$DyedSlotDemo::new)
   );
   public static final StreamCodec<RegistryFriendlyByteBuf, SlotDisplay$DyedSlotDemo> STREAM_CODEC = StreamCodec.composite(
      SlotDisplay.STREAM_CODEC, SlotDisplay$DyedSlotDemo::dye, SlotDisplay.STREAM_CODEC, SlotDisplay$DyedSlotDemo::target, SlotDisplay$DyedSlotDemo::new
   );
   public static final SlotDisplay$Type<SlotDisplay$DyedSlotDemo> TYPE = new SlotDisplay$Type<>(MAP_CODEC, STREAM_CODEC);

   @Override
   public SlotDisplay$Type<SlotDisplay$DyedSlotDemo> type() {
      return TYPE;
   }

   @Override
   public <T> Stream<T> resolve(final ContextMap context, final DisplayContentsFactory<T> factory) {
      BinaryOperator<ItemStack> transformation = (target, dye) -> {
         DyeColor dyeValue = dye.getOrDefault(DataComponents.DYE, DyeColor.WHITE);
         return DyedItemColor.applyDyes(target.copy(), List.of(dyeValue));
      };
      return SlotDisplay.applyDemoTransformation(context, factory, this.target, this.dye, transformation);
   }
}
