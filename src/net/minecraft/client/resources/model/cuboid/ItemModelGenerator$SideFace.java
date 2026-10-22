package net.minecraft.client.resources.model.cuboid;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
record ItemModelGenerator$SideFace(ItemModelGenerator$SideDirection facing, int x, int y) {
}
