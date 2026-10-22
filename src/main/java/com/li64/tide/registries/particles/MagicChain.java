package com.li64.tide.registries.particles;

import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/** A chain shooting out from a frozen mob (Enchanted Pocket Watch), at a random angle, fading away.
 * (1.21.1 grew the chain out over a few ticks; 26.x particles are square quads, so it slides out instead.) */
public class MagicChain extends SingleQuadParticle {
    private static final float OFFSET = 1.0f;
    private static final float MOVE_SPEED = 6f;

    private final float rotation;

    protected MagicChain(ClientLevel level, double x, double y, double z, SpriteSet sprites, float quadSize, float rotation) {
        super(level, x, y, z, 0, 0, 0, sprites.first());
        this.lifetime = 18;
        this.hasPhysics = false;
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
        this.quadSize = quadSize;
        this.rotation = rotation;
        this.setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        super.tick();
        this.alpha = 1f - (float) this.age / this.lifetime;
    }

    @Override
    protected @NotNull Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    @Override
    public @NotNull FacingCameraMode getFacingCameraMode() {
        return (q, cam, partialTick) -> q.set(cam.rotation()).mul(Axis.ZP.rotationDegrees(rotation));
    }

    @Override
    public void extract(QuadParticleRenderState state, Camera camera, float partialTick) {
        Quaternionf q = new Quaternionf();
        this.getFacingCameraMode().setRotation(q, camera, partialTick);
        Vec3 cam = camera.position();
        float moveT = Mth.clamp((this.age + partialTick) * MOVE_SPEED / this.lifetime, 0f, 1f);
        float size = this.getQuadSize(partialTick);
        // slide out along the chain's own "up" from the mob's centre
        Vector3f out = new Vector3f(0, (OFFSET + moveT - 1f) * size, 0).rotate(q);
        float x = (float) (Mth.lerp(partialTick, this.xo, this.x) - cam.x()) + out.x;
        float y = (float) (Mth.lerp(partialTick, this.yo, this.y) - cam.y()) + out.y;
        float z = (float) (Mth.lerp(partialTick, this.zo, this.z) - cam.z()) + out.z;
        this.extractRotatedQuad(state, q, x, y, z, partialTick);
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {
        @Override
        public @NotNull Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level,
                                                double x, double y, double z, double vx, double vy, double vz, RandomSource random) {
            return new MagicChain(level, x, y, z, sprites,
                    1.0f + random.nextFloat() * 0.6f,
                    (random.nextFloat() - 0.5f) * 200f);
        }
    }
}
