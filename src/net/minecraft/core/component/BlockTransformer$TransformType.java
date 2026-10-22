package net.minecraft.core.component;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.function.IntFunction;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.ByIdMap$OutOfBoundsStrategy;
import net.minecraft.util.StringRepresentable;

public enum BlockTransformer$TransformType implements StringRepresentable {
   SINGLE_BLOCK(0, "single_block"),
   COPPER_CHEST(1, "copper_chest");

   private final int id;
   private final String name;
   public static final Codec<BlockTransformer$TransformType> CODEC = StringRepresentable.fromValues(BlockTransformer$TransformType::values);
   private static final IntFunction<BlockTransformer$TransformType> BY_ID = ByIdMap.continuous(
      BlockTransformer$TransformType::getId, values(), ByIdMap$OutOfBoundsStrategy.ZERO
   );
   public static final StreamCodec<ByteBuf, BlockTransformer$TransformType> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, BlockTransformer$TransformType::getId);

   private BlockTransformer$TransformType(final int id, final String name) {
      this.id = id;
      this.name = name;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }

   public int getId() {
      return this.id;
   }
}
