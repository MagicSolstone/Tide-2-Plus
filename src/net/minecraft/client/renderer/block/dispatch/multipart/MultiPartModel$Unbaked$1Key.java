package net.minecraft.client.renderer.block.dispatch.multipart;

import it.unimi.dsi.fastutil.ints.IntList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
record MultiPartModel$Unbaked$1Key(MultiPartModel$Unbaked model, IntList selectors) {
}
