package net.minecraft.core.component.predicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.advancements.predicates.MinMaxBounds$Doubles;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation;
import net.minecraft.world.item.component.ItemAttributeModifiers$Entry;

public record AttributeModifiersPredicate$EntryPredicate(
   Optional<HolderSet<Attribute>> attribute,
   Optional<Identifier> id,
   MinMaxBounds$Doubles amount,
   Optional<AttributeModifier$Operation> operation,
   Optional<EquipmentSlotGroup> slot
) implements Predicate<ItemAttributeModifiers$Entry> {
   public static final Codec<AttributeModifiersPredicate$EntryPredicate> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            RegistryCodecs.holderSet(Registries.ATTRIBUTE).optionalFieldOf("attribute").forGetter(AttributeModifiersPredicate$EntryPredicate::attribute),
            Identifier.CODEC.optionalFieldOf("id").forGetter(AttributeModifiersPredicate$EntryPredicate::id),
            MinMaxBounds$Doubles.CODEC.optionalFieldOf("amount", MinMaxBounds$Doubles.ANY).forGetter(AttributeModifiersPredicate$EntryPredicate::amount),
            AttributeModifier$Operation.CODEC.optionalFieldOf("operation").forGetter(AttributeModifiersPredicate$EntryPredicate::operation),
            EquipmentSlotGroup.CODEC.optionalFieldOf("slot").forGetter(AttributeModifiersPredicate$EntryPredicate::slot)
         )
         .apply(i, AttributeModifiersPredicate$EntryPredicate::new)
   );

   public boolean test(final ItemAttributeModifiers$Entry value) {
      if (this.attribute.isPresent() && !this.attribute.get().contains(value.attribute())) {
         return false;
      } else if (this.id.isPresent() && !this.id.get().equals(value.modifier().id())) {
         return false;
      } else if (!this.amount.matches(value.modifier().amount())) {
         return false;
      } else {
         return this.operation.isPresent() && this.operation.get() != value.modifier().operation()
            ? false
            : !this.slot.isPresent() || this.slot.get() == value.slot();
      }
   }
}
