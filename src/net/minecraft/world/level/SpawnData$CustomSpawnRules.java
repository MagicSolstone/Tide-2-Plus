package net.minecraft.world.level;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.InclusiveRange;

public record SpawnData$CustomSpawnRules(InclusiveRange<Integer> blockLightLimit, InclusiveRange<Integer> skyLightLimit) {
   private static final InclusiveRange<Integer> LIGHT_RANGE = new InclusiveRange<>(0, 15);
   public static final Codec<SpawnData$CustomSpawnRules> CODEC = RecordCodecBuilder.create(
      i -> i.group(lightLimit("block_light_limit").forGetter(o -> o.blockLightLimit), lightLimit("sky_light_limit").forGetter(o -> o.skyLightLimit))
         .apply(i, SpawnData$CustomSpawnRules::new)
   );

   private static DataResult<InclusiveRange<Integer>> checkLightBoundaries(final InclusiveRange<Integer> range) {
      return !LIGHT_RANGE.contains(range) ? DataResult.error(() -> "Light values must be withing range " + LIGHT_RANGE) : DataResult.success(range);
   }

   private static MapCodec<InclusiveRange<Integer>> lightLimit(final String name) {
      return InclusiveRange.INT.lenientOptionalFieldOf(name, LIGHT_RANGE).validate(SpawnData$CustomSpawnRules::checkLightBoundaries);
   }

   public boolean isValidPosition(final BlockPos blockSpawnPos, final ServerLevel level) {
      return this.blockLightLimit.isValueInRange(level.getBrightness(LightLayer.BLOCK, blockSpawnPos))
         && this.skyLightLimit.isValueInRange(level.getEffectiveSkyBrightness(blockSpawnPos));
   }
}
