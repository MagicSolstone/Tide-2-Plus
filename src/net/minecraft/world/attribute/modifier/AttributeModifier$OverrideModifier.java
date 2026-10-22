package net.minecraft.world.attribute.modifier;

import com.mojang.serialization.Codec;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.LerpFunction;

public record AttributeModifier$OverrideModifier<Value>() implements AttributeModifier<Value, Value> {
   private static final AttributeModifier$OverrideModifier<?> INSTANCE = new AttributeModifier$OverrideModifier();

   @Override
   public Value apply(final Value subject, final Value argument) {
      return argument;
   }

   @Override
   public Codec<Value> argumentCodec(final EnvironmentAttribute<Value> attribute) {
      return attribute.valueCodec();
   }

   @Override
   public LerpFunction<Value> argumentKeyframeLerp(final EnvironmentAttribute<Value> attribute) {
      return attribute.type().keyframeLerp();
   }
}
