package net.minecraft.world.attribute;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Util;
import net.minecraft.world.attribute.modifier.AttributeModifier;

public record EnvironmentAttributeMap$Entry<Value, Argument>(Argument argument, AttributeModifier<Value, Argument> modifier) {
   private static <Value> Codec<EnvironmentAttributeMap$Entry<Value, ?>> createCodec(final EnvironmentAttribute<Value> attribute) {
      Codec<EnvironmentAttributeMap$Entry<Value, ?>> fullCodec = attribute.type()
         .modifierCodec()
         .dispatch("modifier", EnvironmentAttributeMap$Entry::modifier, Util.memoize(modifier -> createFullCodec(attribute, modifier)));
      return Codec.either(attribute.valueCodec(), fullCodec)
         .xmap(
            either -> (EnvironmentAttributeMap$Entry)either.map(value -> new EnvironmentAttributeMap$Entry<>(value, AttributeModifier.override()), e -> e),
            entry -> entry.modifier == AttributeModifier.override() ? Either.left(entry.argument()) : Either.right(entry)
         );
   }

   private static <Value, Argument> MapCodec<EnvironmentAttributeMap$Entry<Value, Argument>> createFullCodec(
      final EnvironmentAttribute<Value> attribute, final AttributeModifier<Value, Argument> modifier
   ) {
      return RecordCodecBuilder.mapCodec(
         i -> i.group(modifier.argumentCodec(attribute).fieldOf("argument").forGetter(EnvironmentAttributeMap$Entry::argument))
            .apply(i, value -> new EnvironmentAttributeMap$Entry<>(value, modifier))
      );
   }

   public Value applyModifier(final Value subject) {
      return this.modifier.apply(subject, this.argument);
   }
}
