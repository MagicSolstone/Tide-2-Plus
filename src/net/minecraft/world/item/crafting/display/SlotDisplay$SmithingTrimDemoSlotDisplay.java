package net.minecraft.world.item.crafting.display;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SmithingTrimRecipe;
import net.minecraft.world.item.equipment.trim.TrimPattern;

public record SlotDisplay$SmithingTrimDemoSlotDisplay(SlotDisplay base, SlotDisplay material, Holder<TrimPattern> pattern) implements SlotDisplay {
   public static final MapCodec<SlotDisplay$SmithingTrimDemoSlotDisplay> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            SlotDisplay.CODEC.fieldOf("base").forGetter(SlotDisplay$SmithingTrimDemoSlotDisplay::base),
            SlotDisplay.CODEC.fieldOf("material").forGetter(SlotDisplay$SmithingTrimDemoSlotDisplay::material),
            TrimPattern.CODEC.fieldOf("pattern").forGetter(SlotDisplay$SmithingTrimDemoSlotDisplay::pattern)
         )
         .apply(i, SlotDisplay$SmithingTrimDemoSlotDisplay::new)
   );
   public static final StreamCodec<RegistryFriendlyByteBuf, SlotDisplay$SmithingTrimDemoSlotDisplay> STREAM_CODEC = StreamCodec.composite(
      SlotDisplay.STREAM_CODEC,
      SlotDisplay$SmithingTrimDemoSlotDisplay::base,
      SlotDisplay.STREAM_CODEC,
      SlotDisplay$SmithingTrimDemoSlotDisplay::material,
      TrimPattern.STREAM_CODEC,
      SlotDisplay$SmithingTrimDemoSlotDisplay::pattern,
      SlotDisplay$SmithingTrimDemoSlotDisplay::new
   );
   public static final SlotDisplay$Type<SlotDisplay$SmithingTrimDemoSlotDisplay> TYPE = new SlotDisplay$Type<>(MAP_CODEC, STREAM_CODEC);

   @Override
   public SlotDisplay$Type<SlotDisplay$SmithingTrimDemoSlotDisplay> type() {
      return TYPE;
   }

   @Override
   public <T> Stream<T> resolve(final ContextMap context, final DisplayContentsFactory<T> factory) {
      RandomSource randomSource = RandomSource.createThreadLocalInstance(System.identityHashCode(this));
      BinaryOperator<ItemStack> transformation = (base, material) -> SmithingTrimRecipe.applyTrim(base, material, this.pattern);
      return SlotDisplay.applyDemoTransformation(context, factory, this.base, this.material, randomSource, transformation);
   }
}
