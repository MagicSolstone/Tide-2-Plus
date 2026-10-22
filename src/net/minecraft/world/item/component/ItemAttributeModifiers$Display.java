package net.minecraft.world.item.component;

import com.mojang.serialization.Codec;
import java.util.function.Consumer;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.Nullable;

public interface ItemAttributeModifiers$Display {
   Codec<ItemAttributeModifiers$Display> CODEC = ItemAttributeModifiers$Display$Type.CODEC.dispatch(ItemAttributeModifiers$Display::type, type -> type.codec);
   StreamCodec<RegistryFriendlyByteBuf, ItemAttributeModifiers$Display> STREAM_CODEC = ItemAttributeModifiers$Display$Type.STREAM_CODEC
      .<RegistryFriendlyByteBuf>cast()
      .dispatch(ItemAttributeModifiers$Display::type, ItemAttributeModifiers$Display$Type::streamCodec);

   static ItemAttributeModifiers$Display attributeModifiers() {
      return ItemAttributeModifiers$Display$Default.INSTANCE;
   }

   static ItemAttributeModifiers$Display hidden() {
      return ItemAttributeModifiers$Display$Hidden.INSTANCE;
   }

   static ItemAttributeModifiers$Display override(final Component component) {
      return new ItemAttributeModifiers$Display$OverrideText(component);
   }

   ItemAttributeModifiers$Display$Type type();

   void apply(Consumer<Component> consumer, @Nullable Player player, Holder<Attribute> attribute, AttributeModifier modifier);
}
