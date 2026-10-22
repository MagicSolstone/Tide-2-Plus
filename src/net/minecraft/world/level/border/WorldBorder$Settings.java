package net.minecraft.world.level.border;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record WorldBorder$Settings(
   double centerX, double centerZ, double damagePerBlock, double safeZone, int warningBlocks, int warningTime, double size, long lerpTime, double lerpTarget
) {
   public static final WorldBorder$Settings DEFAULT = new WorldBorder$Settings(0.0, 0.0, 0.2, 5.0, 5, 300, 5.999997E7F, 0L, 0.0);
   public static final Codec<WorldBorder$Settings> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Codec.doubleRange(-2.9999984E7, 2.9999984E7).fieldOf("center_x").forGetter(WorldBorder$Settings::centerX),
            Codec.doubleRange(-2.9999984E7, 2.9999984E7).fieldOf("center_z").forGetter(WorldBorder$Settings::centerZ),
            Codec.DOUBLE.fieldOf("damage_per_block").forGetter(WorldBorder$Settings::damagePerBlock),
            Codec.DOUBLE.fieldOf("safe_zone").forGetter(WorldBorder$Settings::safeZone),
            Codec.INT.fieldOf("warning_blocks").forGetter(WorldBorder$Settings::warningBlocks),
            Codec.INT.fieldOf("warning_time").forGetter(WorldBorder$Settings::warningTime),
            Codec.DOUBLE.fieldOf("size").forGetter(WorldBorder$Settings::size),
            Codec.LONG.fieldOf("lerp_time").forGetter(WorldBorder$Settings::lerpTime),
            Codec.DOUBLE.fieldOf("lerp_target").forGetter(WorldBorder$Settings::lerpTarget)
         )
         .apply(i, WorldBorder$Settings::new)
   );

   public WorldBorder$Settings(final WorldBorder worldBorder) {
      this(
         worldBorder.centerX,
         worldBorder.centerZ,
         worldBorder.damagePerBlock,
         worldBorder.safeZone,
         worldBorder.warningBlocks,
         worldBorder.warningTime,
         worldBorder.extent.getSize(),
         worldBorder.extent.getLerpTime(),
         worldBorder.extent.getLerpTarget()
      );
   }
}
