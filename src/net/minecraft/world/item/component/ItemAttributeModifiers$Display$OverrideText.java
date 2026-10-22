package net.minecraft.world.item.component;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.Consumer;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.Nullable;

public record ItemAttributeModifiers$Display$OverrideText(Component component) implements ItemAttributeModifiers$Display {
   private static final MapCodec<ItemAttributeModifiers$Display$OverrideText> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(ComponentSerialization.CODEC.fieldOf("value").forGetter(ItemAttributeModifiers$Display$OverrideText::component))
         .apply(i, ItemAttributeModifiers$Display$OverrideText::new)
   );
   private static final StreamCodec<RegistryFriendlyByteBuf, ItemAttributeModifiers$Display$OverrideText> STREAM_CODEC = StreamCodec.composite(
      ComponentSerialization.STREAM_CODEC, ItemAttributeModifiers$Display$OverrideText::component, ItemAttributeModifiers$Display$OverrideText::new
   );

   @Override
   public ItemAttributeModifiers$Display$Type type() {
      return ItemAttributeModifiers$Display$Type.OVERRIDE;
   }

   @Override
   public void apply(final Consumer<Component> consumer, @Nullable final Player player, final Holder<Attribute> attribute, final AttributeModifier modifier) {
      consumer.accept(this.component);
   }
}
