package com.li64.tide.registries.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;

/** A ripple lying flat on the surface of the void (faces up, turns with the camera's yaw). */
public class VoidRipple extends SingleQuadParticle {
    private final SpriteSet sprites;

    protected VoidRipple(ClientLevel level, double x, double y, double z, SpriteSet sprites, float quadSize) {
        super(level, x, y, z, 0, 0, 0, sprites.first());
        this.lifetime = 8;
        this.hasPhysics = false;
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
        this.sprites = sprites;
        this.quadSize = quadSize;
        this.setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.sprites);
    }

    @Override
    protected @NotNull Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    @Override
    public @NotNull FacingCameraMode getFacingCameraMode() {
        return (q, cam, partialTick) -> q.set(0, 0, 0, cam.rotation().w).rotateLocalX(-Mth.HALF_PI);
    }

    public record LargeProvider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {
        @Override
        public @NotNull Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level,
                                                double x, double y, double z, double vx, double vy, double vz, RandomSource random) {
            return new VoidRipple(level, x, y, z, sprites, 0.5f);
        }
    }

    public record SmallProvider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {
        @Override
        public @NotNull Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level,
                                                double x, double y, double z, double vx, double vy, double vz, RandomSource random) {
            return new VoidRipple(level, x, y, z, sprites, 0.25f);
        }
    }
}
