package net.minecraft.world.item.component;

import com.mojang.serialization.MapCodec;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.Nullable;

public record ItemAttributeModifiers$Display$Default() implements ItemAttributeModifiers$Display {
   private static final ItemAttributeModifiers$Display$Default INSTANCE = new ItemAttributeModifiers$Display$Default();
   private static final MapCodec<ItemAttributeModifiers$Display$Default> CODEC = MapCodec.unit(INSTANCE);
   private static final StreamCodec<RegistryFriendlyByteBuf, ItemAttributeModifiers$Display$Default> STREAM_CODEC = StreamCodec.unit(INSTANCE);

   @Override
   public ItemAttributeModifiers$Display$Type type() {
      return ItemAttributeModifiers$Display$Type.DEFAULT;
   }

   @Override
   public void apply(final Consumer<Component> consumer, @Nullable final Player player, final Holder<Attribute> attribute, final AttributeModifier modifier) {
      double amount = modifier.amount();
      boolean displayWithBase = false;
      if (player != null) {
         if (modifier.is(Item.BASE_ATTACK_DAMAGE_ID)) {
            amount += player.getAttributeBaseValue(Attributes.ATTACK_DAMAGE);
            displayWithBase = true;
         } else if (modifier.is(Item.BASE_ATTACK_SPEED_ID)) {
            amount += player.getAttributeBaseValue(Attributes.ATTACK_SPEED);
            displayWithBase = true;
         }
      }

      double displayAmount;
      if (modifier.operation() == AttributeModifier$Operation.ADD_MULTIPLIED_BASE || modifier.operation() == AttributeModifier$Operation.ADD_MULTIPLIED_TOTAL) {
         displayAmount = amount * 100.0;
      } else if (attribute.is(Attributes.KNOCKBACK_RESISTANCE)) {
         displayAmount = amount * 10.0;
      } else {
         displayAmount = amount;
      }

      if (displayWithBase) {
         consumer.accept(
            CommonComponents.space()
               .append(
                  Component.translatable(
                     "attribute.modifier.equals." + modifier.operation().id(),
                     ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(displayAmount),
                     Component.translatable(attribute.value().getDescriptionId())
                  )
               )
               .withStyle(ChatFormatting.DARK_GREEN)
         );
      } else if (amount > 0.0) {
         consumer.accept(
            Component.translatable(
                  "attribute.modifier.plus." + modifier.operation().id(),
                  ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(displayAmount),
                  Component.translatable(attribute.value().getDescriptionId())
               )
               .withStyle(attribute.value().getStyle(true))
         );
      } else if (amount < 0.0) {
         consumer.accept(
            Component.translatable(
                  "attribute.modifier.take." + modifier.operation().id(),
                  ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(-displayAmount),
                  Component.translatable(attribute.value().getDescriptionId())
               )
               .withStyle(attribute.value().getStyle(false))
         );
      }
   }
}
