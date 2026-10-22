package net.minecraft.server.chase;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

record ChaseClient$TeleportTarget(ResourceKey<Level> level, Vec3 pos, Vec2 rot) {
}
