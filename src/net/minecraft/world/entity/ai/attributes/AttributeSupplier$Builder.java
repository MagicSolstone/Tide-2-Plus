package net.minecraft.world.entity.ai.attributes;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import net.minecraft.core.Holder;

public class AttributeSupplier$Builder {
   private final Builder<Holder<Attribute>, AttributeInstance> builder = ImmutableMap.builder();
   private boolean instanceFrozen;

   private AttributeInstance create(final Holder<Attribute> attribute) {
      AttributeInstance result = new AttributeInstance(attribute, attributeInstance -> {
         if (this.instanceFrozen) {
            throw new UnsupportedOperationException("Tried to change value for default attribute instance: " + attribute.getRegisteredName());
         }
      });
      this.builder.put(attribute, result);
      return result;
   }

   public AttributeSupplier$Builder add(final Holder<Attribute> attribute) {
      this.create(attribute);
      return this;
   }

   public AttributeSupplier$Builder add(final Holder<Attribute> attribute, final double baseValue) {
      AttributeInstance result = this.create(attribute);
      result.setBaseValue(baseValue);
      return this;
   }

   public AttributeSupplier build() {
      this.instanceFrozen = true;
      return new AttributeSupplier(this.builder.buildKeepingLast());
   }
}
