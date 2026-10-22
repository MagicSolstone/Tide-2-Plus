package net.minecraft.world.entity.animal.fish;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.function.IntFunction;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.ByIdMap$OutOfBoundsStrategy;
import net.minecraft.util.StringRepresentable;

public enum Salmon$Variant implements StringRepresentable {
   SMALL("small", 0, 0.5F),
   MEDIUM("medium", 1, 1.0F),
   LARGE("large", 2, 1.5F);

   public static final Salmon$Variant DEFAULT = MEDIUM;
   public static final Codec<Salmon$Variant> CODEC = StringRepresentable.fromEnum(Salmon$Variant::values);
   private static final IntFunction<Salmon$Variant> BY_ID = ByIdMap.continuous(Salmon$Variant::id, values(), ByIdMap$OutOfBoundsStrategy.CLAMP);
   public static final StreamCodec<ByteBuf, Salmon$Variant> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Salmon$Variant::id);
   private final String name;
   private final int id;
   private final float boundingBoxScale;

   private Salmon$Variant(final String name, final int id, final float boundingBoxScale) {
      this.name = name;
      this.id = id;
      this.boundingBoxScale = boundingBoxScale;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }

   private int id() {
      return this.id;
   }
}
