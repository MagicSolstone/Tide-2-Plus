package net.minecraft.client.data.models.blockstates;

import java.util.List;
import java.util.function.Function;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.block.state.properties.Property;

@Environment(EnvType.CLIENT)
public class PropertyDispatch$C1<V, T1 extends Comparable<T1>> extends PropertyDispatch<V> {
   private final Property<T1> property1;

   private PropertyDispatch$C1(final Property<T1> property1) {
      this.property1 = property1;
   }

   @Override
   public List<Property<?>> getDefinedProperties() {
      return List.of(this.property1);
   }

   public PropertyDispatch$C1<V, T1> select(final T1 value1, final V variants) {
      PropertyValueList key = PropertyValueList.of(this.property1.value(value1));
      this.putValue(key, variants);
      return this;
   }

   public PropertyDispatch<V> generate(final Function<T1, V> generator) {
      this.property1.getPossibleValues().forEach(value1 -> this.select((T1)value1, generator.apply((T1)value1)));
      return this;
   }
}
