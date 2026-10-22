package net.minecraft.core.component.predicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.predicates.CollectionPredicate;
import net.minecraft.advancements.predicates.SingleComponentItemPredicate;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.ItemAttributeModifiers$Entry;

public record AttributeModifiersPredicate(Optional<CollectionPredicate<ItemAttributeModifiers$Entry, AttributeModifiersPredicate$EntryPredicate>> modifiers)
   implements SingleComponentItemPredicate<ItemAttributeModifiers> {
   public static final Codec<AttributeModifiersPredicate> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            CollectionPredicate.codec(AttributeModifiersPredicate$EntryPredicate.CODEC)
               .optionalFieldOf("modifiers")
               .forGetter(AttributeModifiersPredicate::modifiers)
         )
         .apply(i, AttributeModifiersPredicate::new)
   );

   @Override
   public DataComponentType<ItemAttributeModifiers> componentType() {
      return DataComponents.ATTRIBUTE_MODIFIERS;
   }

   public boolean matches(final ItemAttributeModifiers value) {
      return !this.modifiers.isPresent() || this.modifiers.get().test(value.modifiers());
   }
}
