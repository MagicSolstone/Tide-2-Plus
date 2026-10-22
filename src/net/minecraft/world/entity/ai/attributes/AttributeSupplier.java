package net.minecraft.world.entity.ai.attributes;

import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public class AttributeSupplier {
   private final Map<Holder<Attribute>, AttributeInstance> instances;

   private AttributeSupplier(final Map<Holder<Attribute>, AttributeInstance> instances) {
      this.instances = instances;
   }

   private AttributeInstance getAttributeInstance(final Holder<Attribute> attribute) {
      AttributeInstance instance = this.instances.get(attribute);
      if (instance == null) {
         throw new IllegalArgumentException("Can't find attribute " + attribute.getRegisteredName());
      } else {
         return instance;
      }
   }

   public double getValue(final Holder<Attribute> attribute) {
      return this.getAttributeInstance(attribute).getValue();
   }

   public double getBaseValue(final Holder<Attribute> attribute) {
      return this.getAttributeInstance(attribute).getBaseValue();
   }

   public double getModifierValue(final Holder<Attribute> attribute, final Identifier id) {
      AttributeModifier modifier = this.getAttributeInstance(attribute).getModifier(id);
      if (modifier == null) {
         throw new IllegalArgumentException("Can't find modifier " + id + " on attribute " + attribute.getRegisteredName());
      } else {
         return modifier.amount();
      }
   }

   @Nullable
   public AttributeInstance createInstance(final Consumer<AttributeInstance> onDirty, final Holder<Attribute> attribute) {
      AttributeInstance template = this.instances.get(attribute);
      if (template == null) {
         return null;
      } else {
         AttributeInstance result = new AttributeInstance(attribute, onDirty);
         result.replaceFrom(template);
         return result;
      }
   }

   public static AttributeSupplier$Builder builder() {
      return new AttributeSupplier$Builder();
   }

   public boolean hasAttribute(final Holder<Attribute> attribute) {
      return this.instances.containsKey(attribute);
   }

   public boolean hasModifier(final Holder<Attribute> attribute, final Identifier modifier) {
      AttributeInstance attributeInstance = this.instances.get(attribute);
      return attributeInstance != null && attributeInstance.getModifier(modifier) != null;
   }
}
