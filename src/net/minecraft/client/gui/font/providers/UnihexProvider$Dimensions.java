package net.minecraft.client.gui.font.providers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record UnihexProvider$Dimensions(int left, int right) {
   public static final MapCodec<UnihexProvider$Dimensions> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(Codec.INT.fieldOf("left").forGetter(UnihexProvider$Dimensions::left), Codec.INT.fieldOf("right").forGetter(UnihexProvider$Dimensions::right))
         .apply(i, UnihexProvider$Dimensions::new)
   );
   public static final Codec<UnihexProvider$Dimensions> CODEC = MAP_CODEC.codec();

   public int pack() {
      return pack(this.left, this.right);
   }

   public static int pack(final int left, final int right) {
      return (left & 0xFF) << 8 | right & 0xFF;
   }

   public static int left(final int packed) {
      return (byte)(packed >> 8);
   }

   public static int right(final int packed) {
      return (byte)packed;
   }
}
