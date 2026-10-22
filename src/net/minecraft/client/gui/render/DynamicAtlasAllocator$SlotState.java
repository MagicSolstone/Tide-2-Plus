package net.minecraft.client.gui.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum DynamicAtlasAllocator$SlotState {
   EMPTY,
   STALE,
   READY;
}
