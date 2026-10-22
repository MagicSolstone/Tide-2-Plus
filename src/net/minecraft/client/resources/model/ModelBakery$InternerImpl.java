package net.minecraft.client.resources.model;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.model.geometry.BakedQuad$MaterialInfo;
import org.joml.Vector3fc;

@Environment(EnvType.CLIENT)
class ModelBakery$InternerImpl implements ModelBaker$Interner {
   private final Interner<Vector3fc> vectors = Interners.newStrongInterner();
   private final Interner<BakedQuad$MaterialInfo> materialInfos = Interners.newStrongInterner();

   private ModelBakery$InternerImpl() {
   }

   @Override
   public Vector3fc vector(final Vector3fc v) {
      return (Vector3fc)this.vectors.intern(v);
   }

   @Override
   public BakedQuad$MaterialInfo materialInfo(final BakedQuad$MaterialInfo material) {
      return (BakedQuad$MaterialInfo)this.materialInfos.intern(material);
   }
}
