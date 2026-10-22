package net.minecraft.world.entity.animal.cow;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.function.IntFunction;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.ByIdMap$OutOfBoundsStrategy;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public enum MushroomCow$Variant implements StringRepresentable {
   RED("red", 0, Blocks.RED_MUSHROOM.defaultBlockState()),
   BROWN("brown", 1, Blocks.BROWN_MUSHROOM.defaultBlockState());

   public static final MushroomCow$Variant DEFAULT = RED;
   public static final Codec<MushroomCow$Variant> CODEC = StringRepresentable.fromEnum(MushroomCow$Variant::values);
   private static final IntFunction<MushroomCow$Variant> BY_ID = ByIdMap.continuous(MushroomCow$Variant::id, values(), ByIdMap$OutOfBoundsStrategy.CLAMP);
   public static final StreamCodec<ByteBuf, MushroomCow$Variant> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, MushroomCow$Variant::id);
   private final String type;
   private final int id;
   private final BlockState blockState;

   private MushroomCow$Variant(final String type, final int id, final BlockState blockState) {
      this.type = type;
      this.id = id;
      this.blockState = blockState;
   }

   public BlockState getBlockState() {
      return this.blockState;
   }

   @Override
   public String getSerializedName() {
      return this.type;
   }

   private int id() {
      return this.id;
   }

   private static MushroomCow$Variant byId(final int id) {
      return BY_ID.apply(id);
   }
}
