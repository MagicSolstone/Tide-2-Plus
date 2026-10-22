package net.minecraft.client.color;

import com.google.common.collect.Maps;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.item.DyeColor;

@Environment(EnvType.CLIENT)
public enum ColorLerper$Type {
   SHEEP(25, DyeColor.values(), 0.75F),
   MUSIC_NOTE(30, ColorLerper.MUSIC_NOTE_COLORS, 1.25F);

   private final int colorDuration;
   private final Map<DyeColor, Integer> colorByDye;
   private final DyeColor[] colors;

   private ColorLerper$Type(final int colorDuration, final DyeColor[] colors, final float brightness) {
      this.colorDuration = colorDuration;
      this.colorByDye = Maps.newHashMap(
         Arrays.stream(colors).collect(Collectors.toMap(d -> (DyeColor)d, color -> ColorLerper.getModifiedColor(color, brightness)))
      );
      this.colors = colors;
   }

   public final int getColor(final DyeColor dyeColor) {
      return this.colorByDye.get(dyeColor);
   }
}
