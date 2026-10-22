package net.minecraft.world.attribute;

public sealed interface EnvironmentAttributeLayer<Value>
   permits EnvironmentAttributeLayer$Constant,
   EnvironmentAttributeLayer$Positional,
   EnvironmentAttributeLayer$TimeBased {
}
