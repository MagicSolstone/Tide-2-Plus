package net.minecraft.world.level.block.entity;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.function.IntFunction;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.ByIdMap$OutOfBoundsStrategy;
import net.minecraft.util.StringRepresentable;

public enum TestInstanceBlockEntity$Status implements StringRepresentable {
   CLEARED("cleared", 0),
   RUNNING("running", 1),
   FINISHED("finished", 2);

   private static final IntFunction<TestInstanceBlockEntity$Status> ID_MAP = ByIdMap.continuous(s -> s.index, values(), ByIdMap$OutOfBoundsStrategy.ZERO);
   public static final Codec<TestInstanceBlockEntity$Status> CODEC = StringRepresentable.fromEnum(TestInstanceBlockEntity$Status::values);
   public static final StreamCodec<ByteBuf, TestInstanceBlockEntity$Status> STREAM_CODEC = ByteBufCodecs.idMapper(
      TestInstanceBlockEntity$Status::byIndex, s -> s.index
   );
   private final String id;
   private final int index;

   private TestInstanceBlockEntity$Status(final String id, final int index) {
      this.id = id;
      this.index = index;
   }

   @Override
   public String getSerializedName() {
      return this.id;
   }

   public static TestInstanceBlockEntity$Status byIndex(final int index) {
      return ID_MAP.apply(index);
   }
}
