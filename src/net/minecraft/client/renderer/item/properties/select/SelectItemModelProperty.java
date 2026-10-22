package net.minecraft.client.renderer.item.properties.select;

import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public interface SelectItemModelProperty<T> {
   @Nullable
   T get(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity owner, int seed, ItemDisplayContext displayContext);

   Codec<T> valueCodec();

   SelectItemModelProperty$Type<? extends SelectItemModelProperty<T>, T> type();
}
