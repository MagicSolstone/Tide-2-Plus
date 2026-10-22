package net.minecraft.advancements.predicates;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.util.Optional;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.Property;

public class StatePropertiesPredicate$Builder {
   private final Builder<StatePropertiesPredicate$PropertyMatcher> matchers = ImmutableList.builder();

   private StatePropertiesPredicate$Builder() {
   }

   public static StatePropertiesPredicate$Builder properties() {
      return new StatePropertiesPredicate$Builder();
   }

   public StatePropertiesPredicate$Builder hasProperty(final Property<?> property, final String value) {
      this.matchers.add(new StatePropertiesPredicate$PropertyMatcher(property.getName(), new StatePropertiesPredicate$ExactMatcher(value)));
      return this;
   }

   public StatePropertiesPredicate$Builder hasProperty(final Property<Integer> property, final int value) {
      return this.hasProperty(property, Integer.toString(value));
   }

   public StatePropertiesPredicate$Builder hasProperty(final Property<Boolean> property, final boolean value) {
      return this.hasProperty(property, Boolean.toString(value));
   }

   public <T extends Comparable<T> & StringRepresentable> StatePropertiesPredicate$Builder hasProperty(final Property<T> property, final T value) {
      return this.hasProperty(property, value.getSerializedName());
   }

   public Optional<StatePropertiesPredicate> build() {
      return Optional.of(new StatePropertiesPredicate(this.matchers.build()));
   }
}
