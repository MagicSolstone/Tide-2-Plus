package net.minecraft.world.attribute.modifier;

public interface FloatModifier<Argument> extends AttributeModifier<Float, Argument> {
   FloatModifier<FloatWithAlpha> ALPHA_BLEND = new FloatModifier$1();
   FloatModifier<Float> ADD = Float::sum;
   FloatModifier<Float> SUBTRACT = (FloatModifier$Simple)(a, b) -> a - b;
   FloatModifier<Float> MULTIPLY = (FloatModifier$Simple)(a, b) -> a * b;
   FloatModifier<Float> MINIMUM = Math::min;
   FloatModifier<Float> MAXIMUM = Math::max;
}
