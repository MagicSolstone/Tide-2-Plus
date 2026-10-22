package net.minecraft.world.attribute.modifier;

public interface IntegerModifier<Argument> extends AttributeModifier<Integer, Argument> {
   IntegerModifier<Integer> ADD = Integer::sum;
   IntegerModifier<Integer> SUBTRACT = (a, b) -> a - b;
   IntegerModifier<Integer> MULTIPLY = (a, b) -> a * b;
   IntegerModifier<Integer> MINIMUM = Math::min;
   IntegerModifier<Integer> MAXIMUM = Math::max;

   Integer apply(Integer integer, Argument argument);
}
