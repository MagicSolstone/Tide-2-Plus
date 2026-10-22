package net.minecraft.client.data.models.blockstates;

import com.mojang.datafixers.util.Function3;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.block.state.properties.Property;

@Environment(EnvType.CLIENT)
public class PropertyDispatch$C3<V, T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>> extends PropertyDispatch<V> {
   private final Property<T1> property1;
   private final Property<T2> property2;
   private final Property<T3> property3;

   private PropertyDispatch$C3(final Property<T1> property1, final Property<T2> property2, final Property<T3> property3) {
      this.property1 = property1;
      this.property2 = property2;
      this.property3 = property3;
   }

   @Override
   public List<Property<?>> getDefinedProperties() {
      return List.of(this.property1, this.property2, this.property3);
   }

   public PropertyDispatch$C3<V, T1, T2, T3> select(final T1 value1, final T2 value2, final T3 value3, final V variants) {
      PropertyValueList key = PropertyValueList.of(this.property1.value(value1), this.property2.value(value2), this.property3.value(value3));
      this.putValue(key, variants);
      return this;
   }

   public PropertyDispatch<V> generate(final Function3<T1, T2, T3, V> generator) {
      this.property1
         .getPossibleValues()
         .forEach(
            value1 -> this.property2
               .getPossibleValues()
               .forEach(
                  value2 -> this.property3
                     .getPossibleValues()
                     .forEach(value3 -> this.select((T1)value1, (T2)value2, (T3)value3, (V)generator.apply(value1, value2, value3)))
               )
         );
      return this;
   }
}
