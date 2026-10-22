package net.minecraft.world.entity.ai.attributes;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.function.IntFunction;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.ByIdMap$OutOfBoundsStrategy;
import net.minecraft.util.StringRepresentable;

public enum AttributeModifier$Operation implements StringRepresentable {
   ADD_VALUE("add_value", 0),
   ADD_MULTIPLIED_BASE("add_multiplied_base", 1),
   ADD_MULTIPLIED_TOTAL("add_multiplied_total", 2);

   public static final IntFunction<AttributeModifier$Operation> BY_ID = ByIdMap.continuous(
      AttributeModifier$Operation::id, values(), ByIdMap$OutOfBoundsStrategy.ZERO
   );
   public static final StreamCodec<ByteBuf, AttributeModifier$Operation> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, AttributeModifier$Operation::id);
   public static final Codec<AttributeModifier$Operation> CODEC = StringRepresentable.fromEnum(AttributeModifier$Operation::values);
   private final String name;
   private final int id;

   private AttributeModifier$Operation(final String name, final int id) {
      this.name = name;
      this.id = id;
   }

   public int id() {
      return this.id;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }
}
