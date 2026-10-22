package net.minecraft.client.renderer.item;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.model.ResolvableModel;
import org.joml.Matrix4fc;

@Environment(EnvType.CLIENT)
public interface ItemModel$Unbaked extends ResolvableModel {
   MapCodec<? extends ItemModel$Unbaked> type();

   ItemModel bake(ItemModel$BakingContext context, Matrix4fc transformation);
}
