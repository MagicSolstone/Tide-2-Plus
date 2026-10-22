package net.minecraft.server.dialog.input;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.server.dialog.Dialog;

public record NumberRangeInput(int width, Component label, String labelFormat, NumberRangeInput$RangeInfo rangeInfo) implements InputControl {
   public static final MapCodec<NumberRangeInput> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            Dialog.WIDTH_CODEC.optionalFieldOf("width", 200).forGetter(NumberRangeInput::width),
            ComponentSerialization.CODEC.fieldOf("label").forGetter(NumberRangeInput::label),
            Codec.STRING.optionalFieldOf("label_format", "options.generic_value").forGetter(NumberRangeInput::labelFormat),
            NumberRangeInput$RangeInfo.MAP_CODEC.forGetter(NumberRangeInput::rangeInfo)
         )
         .apply(i, NumberRangeInput::new)
   );

   @Override
   public MapCodec<NumberRangeInput> mapCodec() {
      return MAP_CODEC;
   }

   public Component computeLabel(final String value) {
      return Component.translatable(this.labelFormat, this.label, value);
   }
}
