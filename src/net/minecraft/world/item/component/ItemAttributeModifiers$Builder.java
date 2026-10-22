package net.minecraft.world.item.component;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class ItemAttributeModifiers$Builder {
   private final Builder<ItemAttributeModifiers$Entry> entries = ImmutableList.builder();

   private ItemAttributeModifiers$Builder() {
   }

   public ItemAttributeModifiers$Builder add(final Holder<Attribute> attribute, final AttributeModifier modifier, final EquipmentSlotGroup slot) {
      this.entries.add(new ItemAttributeModifiers$Entry(attribute, modifier, slot));
      return this;
   }

   public ItemAttributeModifiers$Builder add(
      final Holder<Attribute> attribute, final AttributeModifier modifier, final EquipmentSlotGroup slot, final ItemAttributeModifiers$Display display
   ) {
      this.entries.add(new ItemAttributeModifiers$Entry(attribute, modifier, slot, display));
      return this;
   }

   public ItemAttributeModifiers build() {
      return new ItemAttributeModifiers(this.entries.build());
   }
}
