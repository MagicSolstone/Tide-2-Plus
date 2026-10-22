package net.minecraft.core.component;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.function.IntFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.ByIdMap$OutOfBoundsStrategy;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public enum BlockTransformer$DropStrategy implements StringRepresentable {
   CLICKED_FACE(0, "clicked_face", BlockTransformer$DropStrategy$ResourcePopper.FROM_FACE),
   FROM_MIDDLE(1, "from_middle", BlockTransformer$DropStrategy$ResourcePopper.FROM_MIDDLE);

   private final int id;
   private final String name;
   private final BlockTransformer$DropStrategy$ResourcePopper resourcePopper;
   public static final Codec<BlockTransformer$DropStrategy> CODEC = StringRepresentable.fromValues(BlockTransformer$DropStrategy::values);
   private static final IntFunction<BlockTransformer$DropStrategy> BY_ID = ByIdMap.continuous(
      BlockTransformer$DropStrategy::getId, values(), ByIdMap$OutOfBoundsStrategy.ZERO
   );
   public static final StreamCodec<ByteBuf, BlockTransformer$DropStrategy> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, BlockTransformer$DropStrategy::getId);

   private BlockTransformer$DropStrategy(final int id, final String name, final BlockTransformer$DropStrategy$ResourcePopper resourcePopper) {
      this.id = id;
      this.name = name;
      this.resourcePopper = resourcePopper;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }

   public int getId() {
      return this.id;
   }

   public void pop(final Level level, final BlockPos pos, final Direction direction, final ItemStack stack) {
      this.resourcePopper.pop(level, pos, direction, stack);
   }
}
