package net.minecraft.client.resources.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface ModelBaker$SharedOperationKey<T> {
   T compute(ModelBaker modelBakery);
}
