package net.minecraft.world.entity.animal.parrot;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.function.IntFunction;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.ByIdMap$OutOfBoundsStrategy;
import net.minecraft.util.StringRepresentable;

public enum Parrot$Variant implements StringRepresentable {
   RED_BLUE(0, "red_blue"),
   BLUE(1, "blue"),
   GREEN(2, "green"),
   YELLOW_BLUE(3, "yellow_blue"),
   GRAY(4, "gray");

   public static final Parrot$Variant DEFAULT = RED_BLUE;
   private static final IntFunction<Parrot$Variant> BY_ID = ByIdMap.continuous(Parrot$Variant::getId, values(), ByIdMap$OutOfBoundsStrategy.CLAMP);
   public static final Codec<Parrot$Variant> CODEC = StringRepresentable.fromEnum(Parrot$Variant::values);
   @Deprecated
   public static final Codec<Parrot$Variant> LEGACY_CODEC = Codec.INT.xmap(BY_ID::apply, Parrot$Variant::getId);
   public static final StreamCodec<ByteBuf, Parrot$Variant> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Parrot$Variant::getId);
   private final int id;
   private final String name;

   private Parrot$Variant(final int id, final String name) {
      this.id = id;
      this.name = name;
   }

   public int getId() {
      return this.id;
   }

   public static Parrot$Variant byId(final int id) {
      return BY_ID.apply(id);
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }
}
