package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.block.state.BlockState;

@Environment(EnvType.CLIENT)
public class BlockMarker extends SingleQuadParticle {
   private final SingleQuadParticle$Layer layer;

   private BlockMarker(final ClientLevel level, final double x, final double y, final double z, final BlockState state) {
      super(level, x, y, z, Minecraft.getInstance().getModelManager().getBlockStateModelSet().getParticleMaterial(state).sprite());
      this.gravity = 0.0F;
      this.lifetime = 80;
      this.hasPhysics = false;
      this.layer = SingleQuadParticle$Layer.bySprite(this.sprite);
   }

   @Override
   public SingleQuadParticle$Layer getLayer() {
      return this.layer;
   }

   @Override
   public float getQuadSize(final float a) {
      return 0.5F;
   }
}
