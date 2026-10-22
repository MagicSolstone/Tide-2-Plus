package net.minecraft.core.component;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.function.IntFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.ByIdMap$OutOfBoundsStrategy;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LevelEvent$Value;
import org.jspecify.annotations.Nullable;

public enum BlockTransformer$TransformParticle implements StringRepresentable {
   NONE(0, "none", 0),
   SCRAPE(1, "scrape", 3005),
   WAX_ON(2, "wax_on", 3003),
   WAX_OFF(3, "wax_off", 3004);

   private final int id;
   private final String name;
   @LevelEvent$Value
   private final int levelEvent;
   public static final Codec<BlockTransformer$TransformParticle> CODEC = StringRepresentable.fromValues(BlockTransformer$TransformParticle::values);
   private static final IntFunction<BlockTransformer$TransformParticle> BY_ID = ByIdMap.continuous(
      BlockTransformer$TransformParticle::getId, values(), ByIdMap$OutOfBoundsStrategy.ZERO
   );
   public static final StreamCodec<ByteBuf, BlockTransformer$TransformParticle> STREAM_CODEC = ByteBufCodecs.idMapper(
      BY_ID, BlockTransformer$TransformParticle::getId
   );

   private BlockTransformer$TransformParticle(final int id, final String name, @LevelEvent$Value final int levelEvent) {
      this.id = id;
      this.name = name;
      this.levelEvent = levelEvent;
   }

   public void send(final Level level, @Nullable final Entity player, final BlockPos pos) {
      if (!this.equals(NONE)) {
         level.levelEvent(player, this.getLevelEvent(), pos, 0);
      }
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }

   public int getId() {
      return this.id;
   }

   @LevelEvent$Value
   public int getLevelEvent() {
      return this.levelEvent;
   }
}
