package net.minecraft.world.level.storage.loot.functions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProvider;

public class SetAttributesFunction extends LootItemConditionalFunction {
   public static final MapCodec<SetAttributesFunction> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> commonFields(i)
         .and(
            i.group(
               SetAttributesFunction$Modifier.CODEC.listOf().fieldOf("modifiers").forGetter(f -> f.modifiers),
               Codec.BOOL.optionalFieldOf("replace", true).forGetter(f -> f.replace)
            )
         )
         .apply(i, SetAttributesFunction::new)
   );
   private final List<SetAttributesFunction$Modifier> modifiers;
   private final boolean replace;

   private SetAttributesFunction(
      final Optional<Holder<LootItemCondition>> condition, final List<SetAttributesFunction$Modifier> modifiers, final boolean replace
   ) {
      super(condition);
      this.modifiers = List.copyOf(modifiers);
      this.replace = replace;
   }

   @Override
   public MapCodec<SetAttributesFunction> codec() {
      return MAP_CODEC;
   }

   @Override
   public void validate(final ValidationContext context) {
      super.validate(context);
      Validatable.validate(context, "modifiers", this.modifiers);
   }

   @Override
   public ItemStack run(final ItemStack itemStack, final LootContext context) {
      if (this.replace) {
         itemStack.set(DataComponents.ATTRIBUTE_MODIFIERS, this.updateModifiers(context, ItemAttributeModifiers.EMPTY));
      } else {
         itemStack.update(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY, itemModifiers -> this.updateModifiers(context, itemModifiers));
      }

      return itemStack;
   }

   private ItemAttributeModifiers updateModifiers(final LootContext context, ItemAttributeModifiers itemModifiers) {
      RandomSource random = context.getRandom();

      for (SetAttributesFunction$Modifier modifier : this.modifiers) {
         EquipmentSlotGroup slot = Util.getRandom(modifier.slots, random);
         itemModifiers = itemModifiers.withModifierAdded(
            modifier.attribute, new AttributeModifier(modifier.id, modifier.amount.value().getFloat(context), modifier.operation), slot
         );
      }

      return itemModifiers;
   }

   public static SetAttributesFunction$ModifierBuilder modifier(
      final Identifier id, final Holder<Attribute> attribute, final AttributeModifier$Operation operation, final Holder<ContextFloatProvider> amount
   ) {
      return new SetAttributesFunction$ModifierBuilder(id, attribute, operation, amount);
   }

   public static SetAttributesFunction$Builder setAttributes() {
      return new SetAttributesFunction$Builder();
   }
}
