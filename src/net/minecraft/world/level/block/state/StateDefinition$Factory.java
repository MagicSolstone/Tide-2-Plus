package net.minecraft.world.level.block.state;

import net.minecraft.world.level.block.state.properties.Property;

public interface StateDefinition$Factory<O, S> {
   S create(O type, Property<?>[] propertyKeys, Comparable<?>[] propertyValues);
}
