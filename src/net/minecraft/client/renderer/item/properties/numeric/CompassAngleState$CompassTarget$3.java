package net.minecraft.client.renderer.item.properties.numeric;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
enum CompassAngleState$CompassTarget$3 {
   private CompassAngleState$CompassTarget$3(final String name) {
   }

   @Override
   public GlobalPos get(final ClientLevel level, final ItemStack itemStack, @Nullable final ItemOwner owner) {
      return level.getRespawnData().globalPos();
   }
}
