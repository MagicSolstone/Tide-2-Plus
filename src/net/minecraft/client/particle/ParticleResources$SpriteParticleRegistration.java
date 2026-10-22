package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.particles.ParticleOptions;

@FunctionalInterface
@Environment(EnvType.CLIENT)
interface ParticleResources$SpriteParticleRegistration<T extends ParticleOptions> {
   ParticleProvider<T> create(SpriteSet spriteSet);
}
