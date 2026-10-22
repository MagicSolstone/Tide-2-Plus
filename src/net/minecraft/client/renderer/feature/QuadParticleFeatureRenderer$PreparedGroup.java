package net.minecraft.client.renderer.feature;

import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.SingleQuadParticle$Layer;
import net.minecraft.client.renderer.StagedVertexBuffer$Draw;
import net.minecraft.client.renderer.texture.AbstractTexture;

@Environment(EnvType.CLIENT)
record QuadParticleFeatureRenderer$PreparedGroup(
   Map<SingleQuadParticle$Layer, StagedVertexBuffer$Draw> layers, Map<SingleQuadParticle$Layer, AbstractTexture> textures, boolean translucent
) {
}
