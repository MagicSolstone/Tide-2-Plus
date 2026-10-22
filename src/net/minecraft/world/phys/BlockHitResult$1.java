package net.minecraft.world.phys;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.codec.StreamCodec;

class BlockHitResult$1 implements StreamCodec<ByteBuf, BlockHitResult> {
   public BlockHitResult decode(final ByteBuf input) {
      BlockPos pos = BlockPos.STREAM_CODEC.decode(input);
      Direction face = Direction.STREAM_CODEC.decode(input);
      float clickX = input.readFloat();
      float clickY = input.readFloat();
      float clickZ = input.readFloat();
      boolean inside = input.readBoolean();
      boolean worldBorder = input.readBoolean();
      return new BlockHitResult(new Vec3((double)pos.getX() + clickX, (double)pos.getY() + clickY, (double)pos.getZ() + clickZ), face, pos, inside, worldBorder);
   }

   public void encode(final ByteBuf output, final BlockHitResult blockHit) {
      BlockPos blockPos = blockHit.getBlockPos();
      BlockPos.STREAM_CODEC.encode(output, blockPos);
      Direction.STREAM_CODEC.encode(output, blockHit.getDirection());
      Vec3 location = blockHit.getLocation();
      output.writeFloat((float)(location.x - blockPos.getX()));
      output.writeFloat((float)(location.y - blockPos.getY()));
      output.writeFloat((float)(location.z - blockPos.getZ()));
      output.writeBoolean(blockHit.isInside());
      output.writeBoolean(blockHit.isWorldBorderHit());
   }
}
