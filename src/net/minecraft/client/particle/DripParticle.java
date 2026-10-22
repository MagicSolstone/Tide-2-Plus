package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

@Environment(EnvType.CLIENT)
public class DripParticle extends SingleQuadParticle {
   private final Fluid type;
   protected boolean isGlowing;

   private DripParticle(final ClientLevel level, final double x, final double y, final double z, final Fluid type, final TextureAtlasSprite sprite) {
      super(level, x, y, z, sprite);
      this.setSize(0.01F, 0.01F);
      this.gravity = 0.06F;
      this.type = type;
   }

   protected Fluid getType() {
      return this.type;
   }

   @Override
   public SingleQuadParticle$Layer getLayer() {
      return SingleQuadParticle$Layer.OPAQUE;
   }

   @Override
   public int getLightCoords(final float a) {
      return this.isGlowing ? LightCoordsUtil.withBlock(super.getLightCoords(a), 15) : super.getLightCoords(a);
   }

   @Override
   public void tick() {
      this.xo = this.x;
      this.yo = this.y;
      this.zo = this.z;
      this.preMoveUpdate();
      if (!this.removed) {
         this.yd = this.yd - this.gravity;
         this.move(this.xd, this.yd, this.zd);
         this.postMoveUpdate();
         if (!this.removed) {
            this.xd *= 0.98F;
            this.yd *= 0.98F;
            this.zd *= 0.98F;
            if (this.type != Fluids.EMPTY) {
               BlockPos pos = BlockPos.containing(this.x, this.y, this.z);
               FluidState fluidState = this.level.getFluidState(pos);
               if (fluidState.is(this.type) && this.y < pos.getY() + fluidState.getHeight(this.level, pos)) {
                  this.remove();
               }
            }
         }
      }
   }

   protected void preMoveUpdate() {
      if (this.lifetime-- <= 0) {
         this.remove();
      }
   }

   protected void postMoveUpdate() {
   }
}
