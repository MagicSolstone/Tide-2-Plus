package net.minecraft.client.color;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;

@Environment(EnvType.CLIENT)
public class ColorLerper {
   public static final DyeColor[] MUSIC_NOTE_COLORS = new DyeColor[]{
      DyeColor.WHITE,
      DyeColor.LIGHT_GRAY,
      DyeColor.LIGHT_BLUE,
      DyeColor.BLUE,
      DyeColor.CYAN,
      DyeColor.GREEN,
      DyeColor.LIME,
      DyeColor.YELLOW,
      DyeColor.ORANGE,
      DyeColor.PINK,
      DyeColor.RED,
      DyeColor.MAGENTA
   };

   public static int getLerpedColor(final ColorLerper$Type type, final float tick) {
      int tickCount = Mth.floor(tick);
      int value = tickCount / type.colorDuration;
      int colorCount = type.colors.length;
      int c1 = value % colorCount;
      int c2 = (value + 1) % colorCount;
      float subStep = (tickCount % type.colorDuration + Mth.frac(tick)) / type.colorDuration;
      int color1 = type.getColor(type.colors[c1]);
      int color2 = type.getColor(type.colors[c2]);
      return ARGB.srgbLerp(subStep, color1, color2);
   }

   private static int getModifiedColor(final DyeColor color, final float brightness) {
      if (color == DyeColor.WHITE) {
         return -1644826;
      } else {
         int src = color.getTextureDiffuseColor();
         return ARGB.color(
            255,
            Mth.clamp(Mth.floor(ARGB.red(src) * brightness), 0, 255),
            Mth.clamp(Mth.floor(ARGB.green(src) * brightness), 0, 255),
            Mth.clamp(Mth.floor(ARGB.blue(src) * brightness), 0, 255)
         );
      }
   }
}
