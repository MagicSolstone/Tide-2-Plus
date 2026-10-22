package net.minecraft.world.level.block.state.properties;

public record Property$Value<T extends Comparable<T>>(Property<T> property, T value) {
   public Property$Value(Property<T> property, T value) {
      if (!property.getPossibleValues().contains(value)) {
         throw new IllegalArgumentException("Value " + value + " does not belong to property " + property);
      } else {
         this.property = property;
         this.value = value;
      }
   }

   @Override
   public String toString() {
      return this.property.getName() + "=" + this.property.getName(this.value);
   }

   public String valueName() {
      return this.property.getName(this.value);
   }
}
