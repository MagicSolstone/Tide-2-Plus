package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos$MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.FloatProviders;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviders;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Column;
import net.minecraft.world.level.levelgen.Column$Range;

public record LargeDripstoneFeature(
   HolderSet<Block> replaceableBlocks,
   int floorToCeilingSearchRange,
   IntProvider columnRadius,
   FloatProvider heightScale,
   float maxColumnRadiusToCaveHeightRatio,
   FloatProvider stalactiteBluntness,
   FloatProvider stalagmiteBluntness,
   FloatProvider windSpeed,
   int minRadiusForWind,
   float minBluntnessForWind
) implements Feature {
   public static final MapCodec<LargeDripstoneFeature> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            RegistryCodecs.holderSet(Registries.BLOCK).fieldOf("replaceable_blocks").forGetter(LargeDripstoneFeature::replaceableBlocks),
            Codec.intRange(1, 512).optionalFieldOf("floor_to_ceiling_search_range", 30).forGetter(LargeDripstoneFeature::floorToCeilingSearchRange),
            IntProviders.codec(1, 16).fieldOf("column_radius").forGetter(LargeDripstoneFeature::columnRadius),
            FloatProviders.codec(0.0F, 20.0F).fieldOf("height_scale").forGetter(LargeDripstoneFeature::heightScale),
            Codec.floatRange(0.1F, 1.0F).fieldOf("max_column_radius_to_cave_height_ratio").forGetter(LargeDripstoneFeature::maxColumnRadiusToCaveHeightRatio),
            FloatProviders.codec(0.1F, 10.0F).fieldOf("stalactite_bluntness").forGetter(LargeDripstoneFeature::stalactiteBluntness),
            FloatProviders.codec(0.1F, 10.0F).fieldOf("stalagmite_bluntness").forGetter(LargeDripstoneFeature::stalagmiteBluntness),
            FloatProviders.codec(0.0F, 2.0F).fieldOf("wind_speed").forGetter(LargeDripstoneFeature::windSpeed),
            Codec.intRange(0, 100).fieldOf("min_radius_for_wind").forGetter(LargeDripstoneFeature::minRadiusForWind),
            Codec.floatRange(0.0F, 5.0F).fieldOf("min_bluntness_for_wind").forGetter(LargeDripstoneFeature::minBluntnessForWind)
         )
         .apply(i, LargeDripstoneFeature::new)
   );

   @Override
   public MapCodec<LargeDripstoneFeature> codec() {
      return CODEC;
   }

   @Override
   public boolean place(final WorldGenLevel level, final ChunkGenerator chunkGenerator, final RandomSource random, final BlockPos origin) {
      if (!SpeleothemUtils.isEmptyOrWater(level, origin)) {
         return false;
      } else {
         Optional<Column> column = Column.scan(
            level,
            origin,
            this.floorToCeilingSearchRange,
            SpeleothemUtils::isEmptyOrWater,
            state -> SpeleothemUtils.isBaseOrLava(state, Blocks.DRIPSTONE_BLOCK, this.replaceableBlocks)
         );
         if (!(!column.isEmpty() && column.get() instanceof Column$Range columnRange)) {
            return false;
         } else if (columnRange.height() < 4) {
            return false;
         } else {
            int maxColumnRadiusBasedOnColumnHeight = (int)(columnRange.height() * this.maxColumnRadiusToCaveHeightRatio);
            int maxColumnRadius = Mth.clamp(maxColumnRadiusBasedOnColumnHeight, this.columnRadius.minInclusive(), this.columnRadius.maxInclusive());
            int radius = Mth.randomBetweenInclusive(random, this.columnRadius.minInclusive(), maxColumnRadius);
            LargeDripstoneFeature$LargeDripstone stalactite = makeDripstone(
               origin.atY(columnRange.ceiling() - 1), false, random, radius, this.stalactiteBluntness, this.heightScale
            );
            LargeDripstoneFeature$LargeDripstone stalagmite = makeDripstone(
               origin.atY(columnRange.floor() + 1), true, random, radius, this.stalagmiteBluntness, this.heightScale
            );
            LargeDripstoneFeature$WindOffsetter wind;
            if (stalactite.isSuitableForWind(this.minRadiusForWind, this.minBluntnessForWind)
               && stalagmite.isSuitableForWind(this.minRadiusForWind, this.minBluntnessForWind)) {
               wind = new LargeDripstoneFeature$WindOffsetter(origin.getY(), random, this.windSpeed, 16 - radius);
            } else {
               wind = LargeDripstoneFeature$WindOffsetter.noWind();
            }

            boolean stalactiteBaseEmbeddedInStone = stalactite.moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(level, wind);
            boolean stalagmiteBaseEmbeddedInStone = stalagmite.moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(level, wind);
            if (stalactiteBaseEmbeddedInStone) {
               stalactite.placeBlocks(level, random, wind);
            }

            if (stalagmiteBaseEmbeddedInStone) {
               stalagmite.placeBlocks(level, random, wind);
            }

            if (SharedConstants.DEBUG_LARGE_DRIPSTONE) {
               this.placeDebugMarkers(level, origin, columnRange, wind);
            }

            return true;
         }
      }
   }

   private static LargeDripstoneFeature$LargeDripstone makeDripstone(
      final BlockPos root,
      final boolean pointingUp,
      final RandomSource random,
      final int radius,
      final FloatProvider bluntness,
      final FloatProvider heightScale
   ) {
      return new LargeDripstoneFeature$LargeDripstone(root, pointingUp, radius, (double)bluntness.sample(random), (double)heightScale.sample(random));
   }

   private void placeDebugMarkers(final WorldGenLevel level, final BlockPos origin, final Column$Range range, final LargeDripstoneFeature$WindOffsetter wind) {
      level.setBlock(wind.offset(origin.atY(range.ceiling() - 1)), Blocks.DIAMOND_BLOCK.defaultBlockState(), 2);
      level.setBlock(wind.offset(origin.atY(range.floor() + 1)), Blocks.GOLD_BLOCK.defaultBlockState(), 2);

      for (BlockPos$MutableBlockPos pos = origin.atY(range.floor() + 2).mutable(); pos.getY() < range.ceiling() - 1; pos.move(Direction.UP)) {
         BlockPos windAdjustedPos = wind.offset(pos);
         if (SpeleothemUtils.isEmptyOrWater(level, windAdjustedPos) || level.getBlockState(windAdjustedPos).is(Blocks.DRIPSTONE_BLOCK)) {
            level.setBlock(windAdjustedPos, Blocks.CREEPER_HEAD.defaultBlockState(), 2);
         }
      }
   }
}
