package net.minecraft.client.renderer.state.level;

import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.SingleQuadParticle$Layer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public class QuadParticleRenderState implements ParticleGroupRenderState {
   private static final int INITIAL_PARTICLE_CAPACITY = 1024;
   private static final int FLOATS_PER_PARTICLE = 12;
   private static final int INTS_PER_PARTICLE = 2;
   private final Map<SingleQuadParticle$Layer, QuadParticleRenderState$Storage> particles = new HashMap<>();
   private int particleCount;

   public void add(
      final SingleQuadParticle$Layer layer,
      final float x,
      final float y,
      final float z,
      final float xRot,
      final float yRot,
      final float zRot,
      final float wRot,
      final float scale,
      final float u0,
      final float u1,
      final float v0,
      final float v1,
      final int color,
      final int lightCoords
   ) {
      this.particles
         .computeIfAbsent(layer, ignored -> new QuadParticleRenderState$Storage())
         .add(x, y, z, xRot, yRot, zRot, wRot, scale, u0, u1, v0, v1, color, lightCoords);
      this.particleCount++;
   }

   @Override
   public void clear() {
      this.particles.values().forEach(QuadParticleRenderState$Storage::clear);
      this.particleCount = 0;
   }

   public boolean isEmpty() {
      return this.particleCount == 0;
   }

   public void buildLayer(final SingleQuadParticle$Layer layer, final VertexConsumer bufferBuilder) {
      QuadParticleRenderState$Storage storage = this.particles.get(layer);
      if (storage != null) {
         storage.forEachParticle(
            (x, y, z, xRot, yRot, zRot, wRot, scale, u0, u1, v0, v1, color, lightCoords) -> this.renderRotatedQuad(
               bufferBuilder, x, y, z, xRot, yRot, zRot, wRot, scale, u0, u1, v0, v1, color, lightCoords
            )
         );
      }
   }

   public Set<SingleQuadParticle$Layer> layers() {
      return this.particles.keySet();
   }

   protected void renderRotatedQuad(
      final VertexConsumer builder,
      final float x,
      final float y,
      final float z,
      final float xRot,
      final float yRot,
      final float zRot,
      final float wRot,
      final float scale,
      final float u0,
      final float u1,
      final float v0,
      final float v1,
      final int color,
      final int lightCoords
   ) {
      Quaternionf rotation = new Quaternionf(xRot, yRot, zRot, wRot);
      this.renderVertex(builder, rotation, x, y, z, 1.0F, -1.0F, scale, u1, v1, color, lightCoords);
      this.renderVertex(builder, rotation, x, y, z, 1.0F, 1.0F, scale, u1, v0, color, lightCoords);
      this.renderVertex(builder, rotation, x, y, z, -1.0F, 1.0F, scale, u0, v0, color, lightCoords);
      this.renderVertex(builder, rotation, x, y, z, -1.0F, -1.0F, scale, u0, v1, color, lightCoords);
   }

   private void renderVertex(
      final VertexConsumer builder,
      final Quaternionf rotation,
      final float x,
      final float y,
      final float z,
      final float nx,
      final float ny,
      final float scale,
      final float u,
      final float v,
      final int color,
      final int lightCoords
   ) {
      Vector3f scratch = new Vector3f(nx, ny, 0.0F).rotate(rotation).mul(scale).add(x, y, z);
      builder.addVertex(scratch.x(), scratch.y(), scratch.z()).setUv(u, v).setColor(color).setLight(lightCoords);
   }

   @Override
   public void submit(final SubmitNodeCollector submitNodeCollector, final CameraRenderState camera) {
      if (this.particleCount > 0) {
         submitNodeCollector.submitQuadParticleGroup(this);
      }
   }
}
