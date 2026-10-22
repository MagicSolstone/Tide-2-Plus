package net.minecraft.world.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation;

public record SulfurCubeArchetype$AttributeEntry(Holder<Attribute> attribute, AttributeModifier modifier) {
   public static final Codec<SulfurCubeArchetype$AttributeEntry> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Attribute.CODEC.fieldOf("attribute").forGetter(SulfurCubeArchetype$AttributeEntry::attribute),
            AttributeModifier.MAP_CODEC.forGetter(SulfurCubeArchetype$AttributeEntry::modifier)
         )
         .apply(i, SulfurCubeArchetype$AttributeEntry::new)
   );

   public static SulfurCubeArchetype$AttributeEntry add(
      final Holder<Attribute> attribute, final double amount, final ResourceKey<SulfurCubeArchetype> archetype
   ) {
      return new SulfurCubeArchetype$AttributeEntry(
         attribute,
         new AttributeModifier(
            Identifier.withDefaultNamespace(archetype.identifier().getPath() + "_add_" + attribute.unwrapKey().get().identifier().getPath()),
            amount,
            AttributeModifier$Operation.ADD_VALUE
         )
      );
   }

   public static SulfurCubeArchetype$AttributeEntry multiply(
      final Holder<Attribute> attribute, final double amount, final ResourceKey<SulfurCubeArchetype> archetype
   ) {
      return new SulfurCubeArchetype$AttributeEntry(
         attribute,
         new AttributeModifier(
            Identifier.withDefaultNamespace(archetype.identifier().getPath() + "_mul_" + attribute.unwrapKey().get().identifier().getPath()),
            amount - 1.0,
            AttributeModifier$Operation.ADD_MULTIPLIED_TOTAL
         )
      );
   }
}
