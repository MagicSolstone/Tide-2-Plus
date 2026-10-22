package net.minecraft.network.syncher;

import io.netty.buffer.ByteBuf;
import java.util.Optional;
import net.minecraft.network.VarInt;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

class EntityDataSerializers$2 implements StreamCodec<ByteBuf, Optional<BlockState>> {
   public void encode(final ByteBuf output, final Optional<BlockState> value) {
      if (value.isPresent()) {
         VarInt.write(output, Block.getId(value.get()));
      } else {
         VarInt.write(output, 0);
      }
   }

   public Optional<BlockState> decode(final ByteBuf input) {
      int id = VarInt.read(input);
      return id == 0 ? Optional.empty() : Optional.of(Block.stateById(id));
   }
}
