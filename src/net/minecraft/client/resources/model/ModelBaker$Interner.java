package net.minecraft.client.resources.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.model.geometry.BakedQuad$MaterialInfo;
import org.joml.Vector3fc;

@Environment(EnvType.CLIENT)
public interface ModelBaker$Interner {
   Vector3fc vector(Vector3fc vector);

   BakedQuad$MaterialInfo materialInfo(BakedQuad$MaterialInfo material);
}
