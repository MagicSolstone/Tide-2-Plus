package net.minecraft.client.multiplayer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.particles.ExplosionParticleInfo;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.phys.Vec3;

@Environment(EnvType.CLIENT)
record ClientExplosionTracker$ExplosionInfo(Vec3 center, float radius, int blockCount, WeightedList<ExplosionParticleInfo> blockParticles) {
}
