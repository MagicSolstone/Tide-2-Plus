package net.minecraft.client.renderer.debug;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.phys.Vec3;

@FunctionalInterface
@Environment(EnvType.CLIENT)
interface GameEventListenerRenderer$ListenerVisitor {
   void accept(Vec3 origin, int radius);
}
