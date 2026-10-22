package net.minecraft.client.data.models.blockstates;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.renderer.block.dispatch.VariantMutator;
import net.minecraft.world.level.block.state.properties.Property;

@Environment(EnvType.CLIENT)
public abstract class PropertyDispatch<V> {
   private final Map<PropertyValueList, V> values = new HashMap<>();

   protected void putValue(final PropertyValueList key, final V variant) {
      V previous = this.values.put(key, variant);
      if (previous != null) {
         throw new IllegalStateException("Value " + key + " is already defined");
      }
   }

   Map<PropertyValueList, V> getEntries() {
      this.verifyComplete();
      return Map.copyOf(this.values);
   }

   private void verifyComplete() {
      List<Property<?>> properties = this.getDefinedProperties();
      Stream<PropertyValueList> valuesToCover = Stream.of(PropertyValueList.EMPTY);

      for (Property<?> property : properties) {
         valuesToCover = valuesToCover.flatMap(current -> property.getAllValues().map(current::extend));
      }

      List<PropertyValueList> undefinedCombinations = valuesToCover.filter(f -> !this.values.containsKey(f)).toList();
      if (!undefinedCombinations.isEmpty()) {
         throw new IllegalStateException("Missing definition for properties: " + undefinedCombinations);
      }
   }

   abstract List<Property<?>> getDefinedProperties();

   public static <T1 extends Comparable<T1>> PropertyDispatch$C1<MultiVariant, T1> initial(final Property<T1> property1) {
      return new PropertyDispatch$C1<>(property1);
   }

   public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>> PropertyDispatch$C2<MultiVariant, T1, T2> initial(
      final Property<T1> property1, final Property<T2> property2
   ) {
      return new PropertyDispatch$C2<>(property1, property2);
   }

   public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>> PropertyDispatch$C3<MultiVariant, T1, T2, T3> initial(
      final Property<T1> property1, final Property<T2> property2, final Property<T3> property3
   ) {
      return new PropertyDispatch$C3<>(property1, property2, property3);
   }

   public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>, T4 extends Comparable<T4>> PropertyDispatch$C4<MultiVariant, T1, T2, T3, T4> initial(
      final Property<T1> property1, final Property<T2> property2, final Property<T3> property3, final Property<T4> property4
   ) {
      return new PropertyDispatch$C4<>(property1, property2, property3, property4);
   }

   public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>, T4 extends Comparable<T4>, T5 extends Comparable<T5>> PropertyDispatch$C5<MultiVariant, T1, T2, T3, T4, T5> initial(
      final Property<T1> property1, final Property<T2> property2, final Property<T3> property3, final Property<T4> property4, final Property<T5> property5
   ) {
      return new PropertyDispatch$C5<>(property1, property2, property3, property4, property5);
   }

   public static <T1 extends Comparable<T1>> PropertyDispatch$C1<VariantMutator, T1> modify(final Property<T1> property1) {
      return new PropertyDispatch$C1<>(property1);
   }

   public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>> PropertyDispatch$C2<VariantMutator, T1, T2> modify(
      final Property<T1> property1, final Property<T2> property2
   ) {
      return new PropertyDispatch$C2<>(property1, property2);
   }

   public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>> PropertyDispatch$C3<VariantMutator, T1, T2, T3> modify(
      final Property<T1> property1, final Property<T2> property2, final Property<T3> property3
   ) {
      return new PropertyDispatch$C3<>(property1, property2, property3);
   }

   public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>, T4 extends Comparable<T4>> PropertyDispatch$C4<VariantMutator, T1, T2, T3, T4> modify(
      final Property<T1> property1, final Property<T2> property2, final Property<T3> property3, final Property<T4> property4
   ) {
      return new PropertyDispatch$C4<>(property1, property2, property3, property4);
   }

   public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>, T4 extends Comparable<T4>, T5 extends Comparable<T5>> PropertyDispatch$C5<VariantMutator, T1, T2, T3, T4, T5> modify(
      final Property<T1> property1, final Property<T2> property2, final Property<T3> property3, final Property<T4> property4, final Property<T5> property5
   ) {
      return new PropertyDispatch$C5<>(property1, property2, property3, property4, property5);
   }
}
