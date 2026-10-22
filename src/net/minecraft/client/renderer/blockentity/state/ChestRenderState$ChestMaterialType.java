package net.minecraft.client.renderer.blockentity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum ChestRenderState$ChestMaterialType {
   ENDER_CHEST,
   CHRISTMAS,
   TRAPPED,
   COPPER_UNAFFECTED,
   COPPER_EXPOSED,
   COPPER_WEATHERED,
   COPPER_OXIDIZED,
   REGULAR;
}
