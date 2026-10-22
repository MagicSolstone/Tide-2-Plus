package net.minecraft.world.level.levelgen.feature;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos$MutableBlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;

public record EndSpikeFeature(List<EndSpikeFeature$EndSpike> spikes, boolean crystalInvulnerable, Optional<BlockPos> crystalBeamTarget) implements Feature {
   private static final int NUMBER_OF_SPIKES = 10;
   private static final int SPIKE_DISTANCE = 42;
   private static final LoadingCache<Long, List<EndSpikeFeature$EndSpike>> SPIKE_CACHE = CacheBuilder.newBuilder()
      .expireAfterWrite(5L, TimeUnit.MINUTES)
      .build(new EndSpikeFeature$SpikeCacheLoader());
   public static final MapCodec<EndSpikeFeature> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            EndSpikeFeature$EndSpike.CODEC.listOf().fieldOf("spikes").forGetter(EndSpikeFeature::spikes),
            Codec.BOOL.optionalFieldOf("crystal_invulnerable", false).forGetter(EndSpikeFeature::crystalInvulnerable),
            BlockPos.CODEC.optionalFieldOf("crystal_beam_target").forGetter(EndSpikeFeature::crystalBeamTarget)
         )
         .apply(i, EndSpikeFeature::new)
   );

   public static List<EndSpikeFeature$EndSpike> getSpikesForLevel(final WorldGenLevel level) {
      RandomSource random = RandomSource.createThreadLocalInstance(level.getSeed());
      long key = random.nextLong() & 65535L;
      return (List<EndSpikeFeature$EndSpike>)SPIKE_CACHE.getUnchecked(key);
   }

   @Override
   public MapCodec<EndSpikeFeature> codec() {
      return CODEC;
   }

   @Override
   public boolean place(final WorldGenLevel level, final ChunkGenerator chunkGenerator, final RandomSource random, final BlockPos origin) {
      List<EndSpikeFeature$EndSpike> spikes = this.spikes;
      if (spikes.isEmpty()) {
         spikes = getSpikesForLevel(level);
      }

      for (EndSpikeFeature$EndSpike spike : spikes) {
         if (spike.isCenterWithinChunk(origin)) {
            this.placeSpike(level, random, spike);
         }
      }

      return true;
   }

   private void placeSpike(final ServerLevelAccessor level, final RandomSource random, final EndSpikeFeature$EndSpike spike) {
      int radius = spike.getRadius();

      for (BlockPos pos : BlockPos.betweenClosed(
         new BlockPos(spike.getCenterX() - radius, level.getMinY(), spike.getCenterZ() - radius),
         new BlockPos(spike.getCenterX() + radius, spike.getHeight() + 10, spike.getCenterZ() + radius)
      )) {
         if (pos.distToLowCornerSqr(spike.getCenterX(), pos.getY(), spike.getCenterZ()) <= radius * radius + 1 && pos.getY() < spike.getHeight()) {
            this.setBlock(level, pos, Blocks.OBSIDIAN.defaultBlockState());
         } else if (pos.getY() > 65) {
            this.setBlock(level, pos, Blocks.AIR.defaultBlockState());
         }
      }

      if (spike.isGuarded()) {
         int start = -2;
         int end = 2;
         int yEnd = 3;
         BlockPos$MutableBlockPos posx = new BlockPos$MutableBlockPos();

         for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
               for (int dy = 0; dy <= 3; dy++) {
                  boolean isXSide = Mth.abs(dx) == 2;
                  boolean isZSide = Mth.abs(dz) == 2;
                  boolean top = dy == 3;
                  if (isXSide || isZSide || top) {
                     boolean xEdge = dx == -2 || dx == 2 || top;
                     boolean zEdge = dz == -2 || dz == 2 || top;
                     BlockState state = Blocks.IRON_BARS
                        .defaultBlockState()
                        .setValue(IronBarsBlock.NORTH, xEdge && dz != -2)
                        .setValue(IronBarsBlock.SOUTH, xEdge && dz != 2)
                        .setValue(IronBarsBlock.WEST, zEdge && dx != -2)
                        .setValue(IronBarsBlock.EAST, zEdge && dx != 2);
                     this.setBlock(level, posx.set(spike.getCenterX() + dx, spike.getHeight() + dy, spike.getCenterZ() + dz), state);
                  }
               }
            }
         }
      }

      EndCrystal endCrystal = EntityTypes.END_CRYSTAL.create(level.getLevel(), EntitySpawnReason.STRUCTURE);
      if (endCrystal != null) {
         endCrystal.setBeamTarget(this.crystalBeamTarget.orElse(null));
         endCrystal.setPermanentlyInvulnerable(this.crystalInvulnerable);
         endCrystal.snapTo(spike.getCenterX() + 0.5, spike.getHeight() + 1, spike.getCenterZ() + 0.5, random.nextFloat() * 360.0F, 0.0F);
         level.addFreshEntity(endCrystal);
         BlockPos crystalPos = endCrystal.blockPosition();
         this.setBlock(level, crystalPos.below(), Blocks.BEDROCK.defaultBlockState());
         this.setBlock(level, crystalPos, FireBlock.getState(level, crystalPos));
      }
   }
}
