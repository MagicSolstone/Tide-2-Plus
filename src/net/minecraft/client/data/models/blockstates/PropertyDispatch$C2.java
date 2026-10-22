package net.minecraft.client.data.models.blockstates;

import java.util.List;
import java.util.function.BiFunction;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.block.state.properties.Property;

@Environment(EnvType.CLIENT)
public class PropertyDispatch$C2<V, T1 extends Comparable<T1>, T2 extends Comparable<T2>> extends PropertyDispatch<V> {
   private final Property<T1> property1;
   private final Property<T2> property2;

   private PropertyDispatch$C2(final Property<T1> property1, final Property<T2> property2) {
      this.property1 = property1;
      this.property2 = property2;
   }

   @Override
   public List<Property<?>> getDefinedProperties() {
      return List.of(this.property1, this.property2);
   }

   public PropertyDispatch$C2<V, T1, T2> select(final T1 value1, final T2 value2, final V variants) {
      PropertyValueList key = PropertyValueList.of(this.property1.value(value1), this.property2.value(value2));
      this.putValue(key, variants);
      return this;
   }

   public PropertyDispatch<V> generate(final BiFunction<T1, T2, V> generator) {
      this.property1
         .getPossibleValues()
         .forEach(value1 -> this.property2.getPossibleValues().forEach(value2 -> this.select((T1)value1, (T2)value2, generator.apply((T1)value1, (T2)value2))));
      return this;
   }
}
