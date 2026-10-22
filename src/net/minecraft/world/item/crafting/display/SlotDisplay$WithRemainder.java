package net.minecraft.world.item.crafting.display;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.flag.FeatureFlagSet;

public record SlotDisplay$WithRemainder(SlotDisplay input, SlotDisplay remainder) implements SlotDisplay {
   public static final MapCodec<SlotDisplay$WithRemainder> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            SlotDisplay.CODEC.fieldOf("input").forGetter(SlotDisplay$WithRemainder::input),
            SlotDisplay.CODEC.fieldOf("remainder").forGetter(SlotDisplay$WithRemainder::remainder)
         )
         .apply(i, SlotDisplay$WithRemainder::new)
   );
   public static final StreamCodec<RegistryFriendlyByteBuf, SlotDisplay$WithRemainder> STREAM_CODEC = StreamCodec.composite(
      SlotDisplay.STREAM_CODEC,
      SlotDisplay$WithRemainder::input,
      SlotDisplay.STREAM_CODEC,
      SlotDisplay$WithRemainder::remainder,
      SlotDisplay$WithRemainder::new
   );
   public static final SlotDisplay$Type<SlotDisplay$WithRemainder> TYPE = new SlotDisplay$Type<>(MAP_CODEC, STREAM_CODEC);

   @Override
   public SlotDisplay$Type<SlotDisplay$WithRemainder> type() {
      return TYPE;
   }

   @Override
   public <T> Stream<T> resolve(final ContextMap context, final DisplayContentsFactory<T> factory) {
      if (factory instanceof DisplayContentsFactory$ForRemainders<T> remainders) {
         List<T> resolvedRemainders = this.remainder.resolve(context, factory).toList();
         return this.input.resolve(context, factory).map(input -> remainders.addRemainder((T)input, resolvedRemainders));
      } else {
         return this.input.resolve(context, factory);
      }
   }

   @Override
   public boolean isEnabled(final FeatureFlagSet enabledFeatures) {
      return this.input.isEnabled(enabledFeatures) && this.remainder.isEnabled(enabledFeatures);
   }
}
