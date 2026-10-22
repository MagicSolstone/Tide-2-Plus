package net.minecraft.world.attribute;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.world.attribute.modifier.AttributeModifier;

public class EnvironmentAttributeMap$Builder {
   private final Map<EnvironmentAttribute<?>, EnvironmentAttributeMap$Entry<?, ?>> entries = new HashMap<>();

   private EnvironmentAttributeMap$Builder() {
   }

   public EnvironmentAttributeMap$Builder putAll(final EnvironmentAttributeMap map) {
      this.entries.putAll(map.entries);
      return this;
   }

   public <Value, Parameter> EnvironmentAttributeMap$Builder modify(
      final EnvironmentAttribute<Value> attribute, final AttributeModifier<Value, Parameter> modifier, final Parameter value
   ) {
      attribute.type().checkAllowedModifier(modifier);
      this.entries.put(attribute, new EnvironmentAttributeMap$Entry<>(value, modifier));
      return this;
   }

   public <Value> EnvironmentAttributeMap$Builder set(final EnvironmentAttribute<Value> attribute, final Value value) {
      return this.modify(attribute, AttributeModifier.override(), value);
   }

   public EnvironmentAttributeMap build() {
      return this.entries.isEmpty() ? EnvironmentAttributeMap.EMPTY : new EnvironmentAttributeMap(Map.copyOf(this.entries));
   }
}
