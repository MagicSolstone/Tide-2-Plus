package net.minecraft.client.renderer.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.Entity;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface EntityRendererProvider<T extends Entity> {
   EntityRenderer<T, ?> create(EntityRendererProvider$Context context);
}
