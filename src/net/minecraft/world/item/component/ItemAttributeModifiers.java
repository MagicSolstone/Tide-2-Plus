package net.minecraft.world.item.component;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.mojang.serialization.Codec;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.apache.commons.lang3.function.TriConsumer;

public record ItemAttributeModifiers(List<ItemAttributeModifiers$Entry> modifiers) {
   public static final ItemAttributeModifiers EMPTY = new ItemAttributeModifiers(List.of());
   public static final Codec<ItemAttributeModifiers> CODEC = ItemAttributeModifiers$Entry.CODEC
      .listOf()
      .xmap(ItemAttributeModifiers::new, ItemAttributeModifiers::modifiers);
   public static final StreamCodec<RegistryFriendlyByteBuf, ItemAttributeModifiers> STREAM_CODEC = StreamCodec.composite(
      ItemAttributeModifiers$Entry.STREAM_CODEC.apply(ByteBufCodecs.list()), ItemAttributeModifiers::modifiers, ItemAttributeModifiers::new
   );
   public static final DecimalFormat ATTRIBUTE_MODIFIER_FORMAT = new DecimalFormat("#.##", DecimalFormatSymbols.getInstance(Locale.ROOT));

   public static ItemAttributeModifiers$Builder builder() {
      return new ItemAttributeModifiers$Builder();
   }

   public ItemAttributeModifiers withModifierAdded(final Holder<Attribute> attribute, final AttributeModifier modifier, final EquipmentSlotGroup slot) {
      com.google.common.collect.ImmutableList.Builder<ItemAttributeModifiers$Entry> newModifiers = ImmutableList.builderWithExpectedSize(
         this.modifiers.size() + 1
      );

      for (ItemAttributeModifiers$Entry entry : this.modifiers) {
         if (!entry.matches(attribute, modifier.id())) {
            newModifiers.add(entry);
         }
      }

      newModifiers.add(new ItemAttributeModifiers$Entry(attribute, modifier, slot));
      return new ItemAttributeModifiers(newModifiers.build());
   }

   public void forEach(final EquipmentSlotGroup slot, final TriConsumer<Holder<Attribute>, AttributeModifier, ItemAttributeModifiers$Display> consumer) {
      for (ItemAttributeModifiers$Entry entry : this.modifiers) {
         if (entry.slot.equals(slot)) {
            consumer.accept(entry.attribute, entry.modifier, entry.display);
         }
      }
   }

   public void forEach(final EquipmentSlotGroup slot, final BiConsumer<Holder<Attribute>, AttributeModifier> consumer) {
      for (ItemAttributeModifiers$Entry entry : this.modifiers) {
         if (entry.slot.equals(slot)) {
            consumer.accept(entry.attribute, entry.modifier);
         }
      }
   }

   public void forEach(final EquipmentSlot slot, final BiConsumer<Holder<Attribute>, AttributeModifier> consumer) {
      for (ItemAttributeModifiers$Entry entry : this.modifiers) {
         if (entry.slot.test(slot)) {
            consumer.accept(entry.attribute, entry.modifier);
         }
      }
   }

   public double compute(final Holder<Attribute> attribute, final double baseValue, final EquipmentSlot slot) {
      double value = baseValue;

      for (ItemAttributeModifiers$Entry entry : this.modifiers) {
         if (entry.slot.test(slot) && entry.attribute == attribute) {
            double amount = entry.modifier.amount();

            value += switch (entry.modifier.operation()) {
               case ADD_VALUE -> amount;
               case ADD_MULTIPLIED_BASE -> amount * baseValue;
               case ADD_MULTIPLIED_TOTAL -> amount * value;
            };
         }
      }

      return value;
   }
}
