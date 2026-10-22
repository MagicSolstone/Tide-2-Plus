package net.minecraft.client.renderer.item.properties.numeric;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.MoonPhase;

@Environment(EnvType.CLIENT)
enum Time$TimeSource$3 {
   private Time$TimeSource$3(final String name) {
   }

   @Override
   public float get(final ClientLevel level, final ItemStack itemStack, final ItemOwner owner, final RandomSource random) {
      return (float)level.environmentAttributes().getValue(EnvironmentAttributes.MOON_PHASE, owner.position()).index() / MoonPhase.COUNT;
   }
}
