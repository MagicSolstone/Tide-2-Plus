package net.minecraft.world.level.storage.loot.functions;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProvider;

public class SetAttributesFunction$ModifierBuilder {
   private final Identifier id;
   private final Holder<Attribute> attribute;
   private final AttributeModifier$Operation operation;
   private final Holder<ContextFloatProvider> amount;
   private final Set<EquipmentSlotGroup> slots = EnumSet.noneOf(EquipmentSlotGroup.class);

   public SetAttributesFunction$ModifierBuilder(
      final Identifier id, final Holder<Attribute> attribute, final AttributeModifier$Operation operation, final Holder<ContextFloatProvider> amount
   ) {
      this.id = id;
      this.attribute = attribute;
      this.operation = operation;
      this.amount = amount;
   }

   public SetAttributesFunction$ModifierBuilder forSlot(final EquipmentSlotGroup slot) {
      this.slots.add(slot);
      return this;
   }

   public SetAttributesFunction$Modifier build() {
      return new SetAttributesFunction$Modifier(this.id, this.attribute, this.operation, this.amount, List.copyOf(this.slots));
   }
}
