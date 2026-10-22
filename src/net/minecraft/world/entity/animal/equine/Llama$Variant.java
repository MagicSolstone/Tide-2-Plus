package net.minecraft.world.entity.animal.equine;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.function.IntFunction;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.ByIdMap$OutOfBoundsStrategy;
import net.minecraft.util.StringRepresentable;

public enum Llama$Variant implements StringRepresentable {
   CREAMY(0, "creamy"),
   WHITE(1, "white"),
   BROWN(2, "brown"),
   GRAY(3, "gray");

   public static final Llama$Variant DEFAULT = CREAMY;
   private static final IntFunction<Llama$Variant> BY_ID = ByIdMap.continuous(Llama$Variant::getId, values(), ByIdMap$OutOfBoundsStrategy.CLAMP);
   public static final Codec<Llama$Variant> CODEC = StringRepresentable.fromEnum(Llama$Variant::values);
   @Deprecated
   public static final Codec<Llama$Variant> LEGACY_CODEC = Codec.INT.xmap(BY_ID::apply, Llama$Variant::getId);
   public static final StreamCodec<ByteBuf, Llama$Variant> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Llama$Variant::getId);
   private final int id;
   private final String name;

   private Llama$Variant(final int id, final String name) {
      this.id = id;
      this.name = name;
   }

   public int getId() {
      return this.id;
   }

   public static Llama$Variant byId(final int id) {
      return BY_ID.apply(id);
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }
}
