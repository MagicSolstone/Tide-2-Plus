package net.minecraft.client.renderer.item.properties.numeric;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class CrossbowPull implements RangeSelectItemModelProperty {
   public static final MapCodec<CrossbowPull> MAP_CODEC = MapCodec.unit(new CrossbowPull());

   @Override
   public float get(final ItemStack itemStack, @Nullable final ClientLevel level, @Nullable final ItemOwner owner, final int seed) {
      LivingEntity entity = owner == null ? null : owner.asLivingEntity();
      if (entity == null) {
         return 0.0F;
      } else if (CrossbowItem.isCharged(itemStack)) {
         return 0.0F;
      } else {
         int chargeDuration = CrossbowItem.getChargeDuration(itemStack, entity);
         return (float)UseDuration.useDuration(itemStack, entity) / chargeDuration;
      }
   }

   @Override
   public MapCodec<CrossbowPull> type() {
      return MAP_CODEC;
   }
}
