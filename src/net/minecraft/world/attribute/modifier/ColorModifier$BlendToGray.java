package net.minecraft.world.attribute.modifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;

public record ColorModifier$BlendToGray(float brightness, float factor) {
   public static final Codec<ColorModifier$BlendToGray> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Codec.floatRange(0.0F, 1.0F).fieldOf("brightness").forGetter(ColorModifier$BlendToGray::brightness),
            Codec.floatRange(0.0F, 1.0F).fieldOf("factor").forGetter(ColorModifier$BlendToGray::factor)
         )
         .apply(i, ColorModifier$BlendToGray::new)
   );

   public static ColorModifier$BlendToGray lerp(final float alpha, final ColorModifier$BlendToGray from, final ColorModifier$BlendToGray to) {
      return new ColorModifier$BlendToGray(Mth.lerp(alpha, from.brightness, to.brightness), Mth.lerp(alpha, from.factor, to.factor));
   }
}
