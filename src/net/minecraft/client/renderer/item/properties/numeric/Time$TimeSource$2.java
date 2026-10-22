package net.minecraft.client.renderer.item.properties.numeric;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
enum Time$TimeSource$2 {
   private Time$TimeSource$2(final String name) {
   }

   @Override
   public float get(final ClientLevel level, final ItemStack itemStack, final ItemOwner owner, final RandomSource random) {
      return level.environmentAttributes().getValue(EnvironmentAttributes.SUN_ANGLE, owner.position()) / 360.0F;
   }
}
