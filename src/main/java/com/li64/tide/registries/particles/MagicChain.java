package com.li64.tide.registries.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

import java.util.function.BiConsumer;

public class MagicChain extends TextureSheetParticle {
    private static final float OFFSET = 1.0f;
    private static final float MOVE_DST = 2.0f;
    private static final float MOVE_SPEED = 6f;

    private final BiConsumer<Quaternionf, Camera> facingCameraFunc;

    protected MagicChain(ClientLevel level, double x, double y, double z, SpriteSet sprites, float quadSize, float rotation) {
        super(level, x, y, z, 0, 0, 0);
        this.lifetime = 18;
        this.hasPhysics = false;
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
        this.quadSize = quadSize;
        this.setSpriteFromAge(sprites);
        this.facingCameraFunc = (q, cam) -> q.set(cam.rotation()).mul(Axis.ZP.rotationDegrees(rotation));
    }

    @Override
    public void tick() {
        super.tick();
        float t = (float) this.age / this.lifetime;
        this.alpha = 1f - t;
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public @NotNull FacingCameraMode getFacingCameraMode() {
        return (q, cam, f) -> facingCameraFunc.accept(q, cam);
    }

    @Override
    public void render(VertexConsumer buffer, Camera cam, float partialTicks) {
        Vec3 camPos = cam.getPosition();
        float x = (float)(Mth.lerp(partialTicks, this.xo, this.x) - camPos.x());
        float y = (float)(Mth.lerp(partialTicks, this.yo, this.y) - camPos.y());
        float z = (float)(Mth.lerp(partialTicks, this.zo, this.z) - camPos.z());
        Quaternionf q = new Quaternionf();
        facingCameraFunc.accept(q, cam);
        if (this.roll != 0.0F) q.rotateZ(Mth.lerp(partialTicks, this.oRoll, this.roll));

        this.renderRotatedQuad(buffer, q, x, y, z, partialTicks);
    }

    protected void renderRotatedQuad(@NotNull VertexConsumer buffer, @NotNull Quaternionf quaternion,
                                     float x, float y, float z, float partialTicks) {
        float size = this.getQuadSize(partialTicks);
        float u0 = this.getU0();
        float u1 = this.getU1();
        float v0 = this.getV0();
        float v1 = this.getV1();
        int color = this.getLightColor(partialTicks);
        float moveT = Mth.clamp(((float) this.age + partialTicks) * MOVE_SPEED / this.lifetime, 0f, 1.0f);
        float yOffset = OFFSET;
        float vOffset = MOVE_DST * moveT;
        final int order = 1;
        this.renderVertex(buffer, quaternion, x, y, z, 1f * order, 1f + yOffset - vOffset, size, u1, v1, color);
        this.renderVertex(buffer, quaternion, x, y, z, 1f * order, 1f + yOffset, size, u1, Mth.lerp(moveT, v1, v0), color);
        this.renderVertex(buffer, quaternion, x, y, z, -1f * order, 1f + yOffset, size, u0, Mth.lerp(moveT, v1, v0), color);
        this.renderVertex(buffer, quaternion, x, y, z, -1f * order, 1f + yOffset - vOffset, size, u0, v1, color);
    }


    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {
        @Override
        public @NotNull Particle createParticle(@NotNull SimpleParticleType type,
                                                @NotNull ClientLevel level,
                                                double x, double y, double z,
                                                double vx, double vy, double vz) {
            return new MagicChain(level, x, y, z, sprites,
                    1.0f + level.getRandom().nextFloat() * 0.6f,
                    (level.getRandom().nextFloat() - 0.5f) * 200f);
        }
    }
}
