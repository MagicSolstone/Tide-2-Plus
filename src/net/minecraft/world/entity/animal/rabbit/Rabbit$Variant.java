package net.minecraft.world.entity.animal.rabbit;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.function.IntFunction;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

public enum Rabbit$Variant implements StringRepresentable {
   BROWN(0, "brown"),
   WHITE(1, "white"),
   BLACK(2, "black"),
   WHITE_SPLOTCHED(3, "white_splotched"),
   GOLD(4, "gold"),
   SALT(5, "salt"),
   EVIL(99, "evil");

   public static final Rabbit$Variant DEFAULT = BROWN;
   private static final IntFunction<Rabbit$Variant> BY_ID = ByIdMap.sparse(Rabbit$Variant::id, values(), DEFAULT);
   public static final Codec<Rabbit$Variant> CODEC = StringRepresentable.fromEnum(Rabbit$Variant::values);
   @Deprecated
   public static final Codec<Rabbit$Variant> LEGACY_CODEC = Codec.INT.xmap(BY_ID::apply, Rabbit$Variant::id);
   public static final StreamCodec<ByteBuf, Rabbit$Variant> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Rabbit$Variant::id);
   private final int id;
   private final String name;

   private Rabbit$Variant(final int id, final String name) {
      this.id = id;
      this.name = name;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }

   public int id() {
      return this.id;
   }

   public static Rabbit$Variant byId(final int id) {
      return BY_ID.apply(id);
   }
}
