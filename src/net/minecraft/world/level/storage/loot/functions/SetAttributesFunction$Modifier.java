package net.minecraft.world.level.storage.loot.functions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation;
import net.minecraft.world.level.storage.loot.LootContextUser;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProvider;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProviders;

record SetAttributesFunction$Modifier(
   Identifier id, Holder<Attribute> attribute, AttributeModifier$Operation operation, Holder<ContextFloatProvider> amount, List<EquipmentSlotGroup> slots
) implements LootContextUser {
   private static final Codec<List<EquipmentSlotGroup>> SLOTS_CODEC = ExtraCodecs.nonEmptyList(ExtraCodecs.compactListCodec(EquipmentSlotGroup.CODEC));
   public static final Codec<SetAttributesFunction$Modifier> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Identifier.CODEC.fieldOf("id").forGetter(SetAttributesFunction$Modifier::id),
            Attribute.CODEC.fieldOf("attribute").forGetter(SetAttributesFunction$Modifier::attribute),
            AttributeModifier$Operation.CODEC.fieldOf("operation").forGetter(SetAttributesFunction$Modifier::operation),
            ContextFloatProviders.CODEC.fieldOf("amount").forGetter(SetAttributesFunction$Modifier::amount),
            SLOTS_CODEC.fieldOf("slot").forGetter(SetAttributesFunction$Modifier::slots)
         )
         .apply(i, SetAttributesFunction$Modifier::new)
   );

   @Override
   public void validate(final ValidationContext context) {
      LootContextUser.super.validate(context);
      Validatable.validateHolder(context, "amount", this.amount);
   }
}
