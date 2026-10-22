package net.minecraft.client.resources.model.geometry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.dispatch.ModelState;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.client.resources.model.sprite.TextureSlots;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface UnbakedGeometry {
   UnbakedGeometry EMPTY = (var0, var1, var2, var3) -> QuadCollection.EMPTY;

   QuadCollection bake(TextureSlots textureSlots, ModelBaker modelBaker, ModelState modelState, ModelDebugName name);
}
