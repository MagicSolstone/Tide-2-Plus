package net.minecraft.client.renderer.item.properties.conditional;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record IsViewEntity() implements ConditionalItemModelProperty {
   public static final MapCodec<IsViewEntity> MAP_CODEC = MapCodec.unit(new IsViewEntity());

   @Override
   public boolean get(
      final ItemStack itemStack, @Nullable final ClientLevel level, @Nullable final LivingEntity owner, final int seed, final ItemDisplayContext displayContext
   ) {
      Minecraft minecraft = Minecraft.getInstance();
      Entity cameraEntity = minecraft.getCameraEntity();
      return cameraEntity != null ? owner == cameraEntity : owner == minecraft.player;
   }

   @Override
   public MapCodec<IsViewEntity> type() {
      return MAP_CODEC;
   }
}
