package net.minecraft.client.resources.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface ResolvableModel {
   void resolveDependencies(ResolvableModel$Resolver resolver);
}
