package net.minecraft.world.item.component;

import com.mojang.serialization.MapCodec;
import java.util.function.Consumer;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.Nullable;

public record ItemAttributeModifiers$Display$Hidden() implements ItemAttributeModifiers$Display {
   private static final ItemAttributeModifiers$Display$Hidden INSTANCE = new ItemAttributeModifiers$Display$Hidden();
   private static final MapCodec<ItemAttributeModifiers$Display$Hidden> CODEC = MapCodec.unit(INSTANCE);
   private static final StreamCodec<RegistryFriendlyByteBuf, ItemAttributeModifiers$Display$Hidden> STREAM_CODEC = StreamCodec.unit(INSTANCE);

   @Override
   public ItemAttributeModifiers$Display$Type type() {
      return ItemAttributeModifiers$Display$Type.HIDDEN;
   }

   @Override
   public void apply(final Consumer<Component> consumer, @Nullable final Player player, final Holder<Attribute> attribute, final AttributeModifier modifier) {
   }
}
