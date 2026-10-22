package net.minecraft.world.level.block.state;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.world.level.block.state.properties.Property;

public class StateDefinition$Builder<O, S extends StateHolder<O, S>> {
   private final O owner;
   private final Map<String, Property<?>> properties = Maps.newHashMap();

   public StateDefinition$Builder(final O owner) {
      this.owner = owner;
   }

   public StateDefinition$Builder<O, S> add(final Property<?>... properties) {
      for (Property<?> property : properties) {
         this.validateProperty(property);
         this.properties.put(property.getName(), property);
      }

      return this;
   }

   private <T extends Comparable<T>> void validateProperty(final Property<T> property) {
      String name = property.getName();
      if (!StateDefinition.NAME_PATTERN.matcher(name).matches()) {
         throw new IllegalArgumentException(this.owner + " has invalidly named property: " + name);
      } else {
         Collection<T> values = property.getPossibleValues();
         if (values.size() <= 1) {
            throw new IllegalArgumentException(this.owner + " attempted use property " + name + " with <= 1 possible values");
         } else {
            for (T comparable : values) {
               String valueName = property.getName(comparable);
               if (!StateDefinition.NAME_PATTERN.matcher(valueName).matches()) {
                  throw new IllegalArgumentException(this.owner + " has property: " + name + " with invalidly named value: " + valueName);
               }
            }

            if (this.properties.containsKey(name)) {
               throw new IllegalArgumentException(this.owner + " has duplicate property: " + name);
            }
         }
      }
   }

   public StateDefinition<O, S> create(final Function<O, S> defaultState, final StateDefinition$Factory<O, S> factory) {
      return new StateDefinition<>(defaultState, this.owner, factory, this.properties);
   }
}
