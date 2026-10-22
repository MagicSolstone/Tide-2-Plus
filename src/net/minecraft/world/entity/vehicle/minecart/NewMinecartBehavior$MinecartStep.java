package net.minecraft.world.entity.vehicle.minecart;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;

public record NewMinecartBehavior$MinecartStep(Vec3 position, Vec3 movement, float yRot, float xRot, float weight) {
   public static final StreamCodec<ByteBuf, NewMinecartBehavior$MinecartStep> STREAM_CODEC = StreamCodec.composite(
      Vec3.STREAM_CODEC,
      NewMinecartBehavior$MinecartStep::position,
      Vec3.STREAM_CODEC,
      NewMinecartBehavior$MinecartStep::movement,
      ByteBufCodecs.ROTATION_BYTE,
      NewMinecartBehavior$MinecartStep::yRot,
      ByteBufCodecs.ROTATION_BYTE,
      NewMinecartBehavior$MinecartStep::xRot,
      ByteBufCodecs.FLOAT,
      NewMinecartBehavior$MinecartStep::weight,
      NewMinecartBehavior$MinecartStep::new
   );
   public static final NewMinecartBehavior$MinecartStep ZERO = new NewMinecartBehavior$MinecartStep(Vec3.ZERO, Vec3.ZERO, 0.0F, 0.0F, 0.0F);
}
