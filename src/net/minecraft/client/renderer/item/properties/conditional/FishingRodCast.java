package net.minecraft.client.renderer.item.properties.conditional;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.entity.FishingHookRenderer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record FishingRodCast() implements ConditionalItemModelProperty {
   public static final MapCodec<FishingRodCast> MAP_CODEC = MapCodec.unit(new FishingRodCast());

   @Override
   public boolean get(
      final ItemStack itemStack, @Nullable final ClientLevel level, @Nullable final LivingEntity owner, final int seed, final ItemDisplayContext displayContext
   ) {
      if (owner instanceof Player player && player.fishing != null) {
         HumanoidArm holdingArm = FishingHookRenderer.getHoldingArm(player);
         return owner.getItemHeldByArm(holdingArm) == itemStack;
      } else {
         return false;
      }
   }

   @Override
   public MapCodec<FishingRodCast> type() {
      return MAP_CODEC;
   }
}
