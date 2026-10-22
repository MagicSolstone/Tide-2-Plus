package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.AABB;

public class EndSpikeFeature$EndSpike {
   public static final Codec<EndSpikeFeature$EndSpike> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Codec.INT.optionalFieldOf("centerX", 0).forGetter(s -> s.centerX),
            Codec.INT.optionalFieldOf("centerZ", 0).forGetter(s -> s.centerZ),
            Codec.INT.optionalFieldOf("radius", 0).forGetter(s -> s.radius),
            Codec.INT.optionalFieldOf("height", 0).forGetter(s -> s.height),
            Codec.BOOL.optionalFieldOf("guarded", false).forGetter(s -> s.guarded)
         )
         .apply(i, EndSpikeFeature$EndSpike::new)
   );
   private final int centerX;
   private final int centerZ;
   private final int radius;
   private final int height;
   private final boolean guarded;
   private final AABB topBoundingBox;

   public EndSpikeFeature$EndSpike(final int centerX, final int centerZ, final int radius, final int height, final boolean guarded) {
      this.centerX = centerX;
      this.centerZ = centerZ;
      this.radius = radius;
      this.height = height;
      this.guarded = guarded;
      this.topBoundingBox = new AABB(centerX - radius, DimensionType.MIN_Y, centerZ - radius, centerX + radius, DimensionType.MAX_Y, centerZ + radius);
   }

   public boolean isCenterWithinChunk(final BlockPos chunkOrigin) {
      return SectionPos.blockToSectionCoord(chunkOrigin.getX()) == SectionPos.blockToSectionCoord(this.centerX)
         && SectionPos.blockToSectionCoord(chunkOrigin.getZ()) == SectionPos.blockToSectionCoord(this.centerZ);
   }

   public int getCenterX() {
      return this.centerX;
   }

   public int getCenterZ() {
      return this.centerZ;
   }

   public int getRadius() {
      return this.radius;
   }

   public int getHeight() {
      return this.height;
   }

   public boolean isGuarded() {
      return this.guarded;
   }

   public AABB getTopBoundingBox() {
      return this.topBoundingBox;
   }
}
