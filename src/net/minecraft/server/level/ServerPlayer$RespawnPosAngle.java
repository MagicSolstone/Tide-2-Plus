package net.minecraft.server.level;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

record ServerPlayer$RespawnPosAngle(Vec3 position, float yaw, float pitch) {
   public static ServerPlayer$RespawnPosAngle of(final Vec3 position, final BlockPos lookAtBlockPos, final float pitch) {
      return new ServerPlayer$RespawnPosAngle(position, calculateLookAtYaw(position, lookAtBlockPos), pitch);
   }

   private static float calculateLookAtYaw(final Vec3 position, final BlockPos lookAtBlockPos) {
      Vec3 lookDirection = Vec3.atBottomCenterOf(lookAtBlockPos).subtract(position).normalize();
      return (float)Mth.wrapDegrees(Mth.atan2(lookDirection.z, lookDirection.x) * 180.0F / (float)Math.PI - 90.0);
   }
}
