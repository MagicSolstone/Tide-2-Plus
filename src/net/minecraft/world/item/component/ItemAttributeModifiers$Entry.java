package net.minecraft.world.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public record ItemAttributeModifiers$Entry(
   Holder<Attribute> attribute, AttributeModifier modifier, EquipmentSlotGroup slot, ItemAttributeModifiers$Display display
) {
   public static final Codec<ItemAttributeModifiers$Entry> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Attribute.CODEC.fieldOf("type").forGetter(ItemAttributeModifiers$Entry::attribute),
            AttributeModifier.MAP_CODEC.forGetter(ItemAttributeModifiers$Entry::modifier),
            EquipmentSlotGroup.CODEC.optionalFieldOf("slot", EquipmentSlotGroup.ANY).forGetter(ItemAttributeModifiers$Entry::slot),
            ItemAttributeModifiers$Display.CODEC
               .optionalFieldOf("display", ItemAttributeModifiers$Display$Default.INSTANCE)
               .forGetter(ItemAttributeModifiers$Entry::display)
         )
         .apply(i, ItemAttributeModifiers$Entry::new)
   );
   public static final StreamCodec<RegistryFriendlyByteBuf, ItemAttributeModifiers$Entry> STREAM_CODEC = StreamCodec.composite(
      Attribute.STREAM_CODEC,
      ItemAttributeModifiers$Entry::attribute,
      AttributeModifier.STREAM_CODEC,
      ItemAttributeModifiers$Entry::modifier,
      EquipmentSlotGroup.STREAM_CODEC,
      ItemAttributeModifiers$Entry::slot,
      ItemAttributeModifiers$Display.STREAM_CODEC,
      ItemAttributeModifiers$Entry::display,
      ItemAttributeModifiers$Entry::new
   );

   public ItemAttributeModifiers$Entry(final Holder<Attribute> attribute, final AttributeModifier modifier, final EquipmentSlotGroup slot) {
      this(attribute, modifier, slot, ItemAttributeModifiers$Display.attributeModifiers());
   }

   public boolean matches(final Holder<Attribute> attribute, final Identifier id) {
      return attribute.equals(this.attribute) && this.modifier.is(id);
   }
}
