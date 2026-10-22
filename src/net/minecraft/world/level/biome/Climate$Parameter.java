package net.minecraft.world.level.biome;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import java.util.Locale;
import net.minecraft.util.ExtraCodecs;
import org.jspecify.annotations.Nullable;

public record Climate$Parameter(long min, long max) {
   public static final Codec<Climate$Parameter> CODEC = ExtraCodecs.intervalCodec(
      Codec.floatRange(-2.0F, 2.0F),
      "min",
      "max",
      (min, max) -> min.compareTo(max) > 0
         ? DataResult.error(() -> "Cannon construct interval, min > max (" + min + " > " + max + ")")
         : DataResult.success(new Climate$Parameter(Climate.quantizeCoord(min), Climate.quantizeCoord(max))),
      p -> Climate.unquantizeCoord(p.min()),
      p -> Climate.unquantizeCoord(p.max())
   );

   public static Climate$Parameter point(final float min) {
      return span(min, min);
   }

   public static Climate$Parameter span(final float min, final float max) {
      if (min > max) {
         throw new IllegalArgumentException("min > max: " + min + " " + max);
      } else {
         return new Climate$Parameter(Climate.quantizeCoord(min), Climate.quantizeCoord(max));
      }
   }

   public static Climate$Parameter span(final Climate$Parameter min, final Climate$Parameter max) {
      if (min.min() > max.max()) {
         throw new IllegalArgumentException("min > max: " + min + " " + max);
      } else {
         return new Climate$Parameter(min.min(), max.max());
      }
   }

   @Override
   public String toString() {
      return this.min == this.max ? String.format(Locale.ROOT, "%d", this.min) : String.format(Locale.ROOT, "[%d-%d]", this.min, this.max);
   }

   public long distance(final long target) {
      long above = target - this.max;
      long below = this.min - target;
      return above > 0L ? above : Math.max(below, 0L);
   }

   public Climate$Parameter span(@Nullable final Climate$Parameter other) {
      return other == null ? this : new Climate$Parameter(Math.min(this.min, other.min()), Math.max(this.max, other.max()));
   }
}
