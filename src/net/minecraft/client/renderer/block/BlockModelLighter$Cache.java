package net.minecraft.client.renderer.block;

import it.unimi.dsi.fastutil.longs.Long2FloatLinkedOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2IntLinkedOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.LightCoordsUtil$BrightnessGetter;
import net.minecraft.util.Util;
import net.minecraft.world.level.block.state.BlockState;

@Environment(EnvType.CLIENT)
public class BlockModelLighter$Cache {
   private boolean enabled;
   private final Long2IntLinkedOpenHashMap colorCache = Util.make(() -> {
      Long2IntLinkedOpenHashMap map = new BlockModelLighter$Cache$1(this, 100, 0.25F);
      map.defaultReturnValue(Integer.MAX_VALUE);
      return map;
   });
   private final Long2FloatLinkedOpenHashMap brightnessCache = Util.make(() -> {
      Long2FloatLinkedOpenHashMap map = new BlockModelLighter$Cache$2(this, 100, 0.25F);
      map.defaultReturnValue(Float.NaN);
      return map;
   });
   private final LightCoordsUtil$BrightnessGetter cachedBrightnessGetter = (level, pos) -> {
      long key = pos.asLong();
      int cached = this.colorCache.get(key);
      if (cached != Integer.MAX_VALUE) {
         return cached;
      } else {
         int value = LightCoordsUtil$BrightnessGetter.DEFAULT.packedBrightness(level, pos);
         if (this.colorCache.size() == 100) {
            this.colorCache.removeFirstInt();
         }

         this.colorCache.put(key, value);
         return value;
      }
   };

   public void enable() {
      this.enabled = true;
   }

   public void disable() {
      this.enabled = false;
      this.colorCache.clear();
      this.brightnessCache.clear();
   }

   public int getLightCoords(final BlockState state, final BlockAndTintGetter level, final BlockPos pos) {
      return LightCoordsUtil.getLightCoords(this.enabled ? this.cachedBrightnessGetter : LightCoordsUtil$BrightnessGetter.DEFAULT, level, state, pos);
   }

   public float getShadeBrightness(final BlockState state, final BlockAndTintGetter level, final BlockPos pos) {
      long key = pos.asLong();
      if (this.enabled) {
         float cached = this.brightnessCache.get(key);
         if (!Float.isNaN(cached)) {
            return cached;
         }
      }

      float brightness = state.getShadeBrightness(level, pos);
      if (this.enabled) {
         if (this.brightnessCache.size() == 100) {
            this.brightnessCache.removeFirstFloat();
         }

         this.brightnessCache.put(key, brightness);
      }

      return brightness;
   }
}
