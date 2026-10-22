package net.minecraft.client.renderer.item.properties.conditional;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record BundleHasSelectedItem() implements ConditionalItemModelProperty {
   public static final MapCodec<BundleHasSelectedItem> MAP_CODEC = MapCodec.unit(new BundleHasSelectedItem());

   @Override
   public boolean get(
      final ItemStack itemStack, @Nullable final ClientLevel level, @Nullable final LivingEntity owner, final int seed, final ItemDisplayContext displayContext
   ) {
      return BundleItem.getSelectedItem(itemStack) != null;
   }

   @Override
   public MapCodec<BundleHasSelectedItem> type() {
      return MAP_CODEC;
   }
}
