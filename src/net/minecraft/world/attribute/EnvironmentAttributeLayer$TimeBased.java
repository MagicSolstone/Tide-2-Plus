package net.minecraft.world.attribute;

@FunctionalInterface
public non-sealed interface EnvironmentAttributeLayer$TimeBased<Value> extends EnvironmentAttributeLayer<Value> {
   Value applyTimeBased(Value baseValue, int cacheTickId);
}
