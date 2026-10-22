package net.minecraft.world.entity.ai.attributes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.ExtraCodecs;

public record AttributeInstance$Packed(Holder<Attribute> attribute, double baseValue, List<AttributeModifier> modifiers) {
   public static final Codec<AttributeInstance$Packed> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            BuiltInRegistries.ATTRIBUTE.holderByNameCodec().fieldOf("id").forGetter(AttributeInstance$Packed::attribute),
            ExtraCodecs.optionalAlwaysPresentFieldOf(Codec.DOUBLE, "base", 0.0).forGetter(AttributeInstance$Packed::baseValue),
            AttributeModifier.CODEC.listOf().optionalFieldOf("modifiers", List.of()).forGetter(AttributeInstance$Packed::modifiers)
         )
         .apply(i, AttributeInstance$Packed::new)
   );
   public static final Codec<List<AttributeInstance$Packed>> LIST_CODEC = CODEC.listOf();
}
