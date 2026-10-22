package net.minecraft.core;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.List;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Util;

public record Rotations(float x, float y, float z) {
   public static final Codec<Rotations> CODEC = Codec.FLOAT
      .listOf()
      .comapFlatMap(
         input -> Util.fixedSize(input, 3).map(floats -> new Rotations((Float)floats.get(0), (Float)floats.get(1), (Float)floats.get(2))),
         rotations -> List.of(rotations.x(), rotations.y(), rotations.z())
      );
   public static final StreamCodec<ByteBuf, Rotations> STREAM_CODEC = new Rotations$1();

   public Rotations(float x, float y, float z) {
      x = !Float.isInfinite(x) && !Float.isNaN(x) ? x % 360.0F : 0.0F;
      y = !Float.isInfinite(y) && !Float.isNaN(y) ? y % 360.0F : 0.0F;
      z = !Float.isInfinite(z) && !Float.isNaN(z) ? z % 360.0F : 0.0F;
      this.x = x;
      this.y = y;
      this.z = z;
   }
}
