package net.minecraft.world.level.block;

import com.google.common.annotations.VisibleForTesting;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class SculkSpreader {
   public static final int MAX_GROWTH_RATE_RADIUS = 24;
   public static final int MAX_CHARGE = 1000;
   public static final float MAX_DECAY_FACTOR = 0.5F;
   private static final int MAX_CURSORS = 32;
   public static final int SHRIEKER_PLACEMENT_RATE = 11;
   public static final int MAX_CURSOR_DISTANCE = 1024;
   private final boolean isWorldGeneration;
   private final TagKey<Block> replaceableBlocks;
   private final int growthSpawnCost;
   private final int noGrowthRadius;
   private final int chargeDecayRate;
   private final int additionalDecayRate;
   private List<SculkSpreader$ChargeCursor> cursors = new ArrayList<>();

   public SculkSpreader(
      final boolean isWorldGeneration,
      final TagKey<Block> replaceableBlocks,
      final int growthSpawnCost,
      final int noGrowthRadius,
      final int chargeDecayRate,
      final int additionalDecayRate
   ) {
      this.isWorldGeneration = isWorldGeneration;
      this.replaceableBlocks = replaceableBlocks;
      this.growthSpawnCost = growthSpawnCost;
      this.noGrowthRadius = noGrowthRadius;
      this.chargeDecayRate = chargeDecayRate;
      this.additionalDecayRate = additionalDecayRate;
   }

   public static SculkSpreader createLevelSpreader() {
      return new SculkSpreader(false, BlockTags.SCULK_REPLACEABLE, 10, 4, 10, 5);
   }

   public static SculkSpreader createWorldGenSpreader() {
      return new SculkSpreader(true, BlockTags.SCULK_REPLACEABLE_WORLD_GEN, 50, 1, 5, 10);
   }

   public TagKey<Block> replaceableBlocks() {
      return this.replaceableBlocks;
   }

   public int growthSpawnCost() {
      return this.growthSpawnCost;
   }

   public int noGrowthRadius() {
      return this.noGrowthRadius;
   }

   public int chargeDecayRate() {
      return this.chargeDecayRate;
   }

   public int additionalDecayRate() {
      return this.additionalDecayRate;
   }

   public boolean isWorldGeneration() {
      return this.isWorldGeneration;
   }

   @VisibleForTesting
   public List<SculkSpreader$ChargeCursor> getCursors() {
      return this.cursors;
   }

   public void clear() {
      this.cursors.clear();
   }

   public void load(final ValueInput input) {
      this.cursors.clear();
      input.<List>read("cursors", SculkSpreader$ChargeCursor.CODEC.sizeLimitedListOf(32)).orElse(List.of()).forEach(this::addCursor);
   }

   public void save(final ValueOutput output) {
      output.store("cursors", SculkSpreader$ChargeCursor.CODEC.listOf(), this.cursors);
      if (SharedConstants.DEBUG_SCULK_CATALYST) {
         int charge = this.getCursors().stream().map(SculkSpreader$ChargeCursor::getCharge).reduce(0, Integer::sum);
         int charges = this.getCursors().stream().map(c -> 1).reduce(0, Integer::sum);
         int max = this.getCursors().stream().map(SculkSpreader$ChargeCursor::getCharge).reduce(0, Math::max);
         output.putInt("stats.total", charge);
         output.putInt("stats.count", charges);
         output.putInt("stats.max", max);
         output.putInt("stats.avg", charge / (charges + 1));
      }
   }

   public void addCursors(final BlockPos startPos, int charge) {
      while (charge > 0) {
         int currentCharge = Math.min(charge, 1000);
         this.addCursor(new SculkSpreader$ChargeCursor(startPos, currentCharge));
         charge -= currentCharge;
      }
   }

   private void addCursor(final SculkSpreader$ChargeCursor cursor) {
      if (this.cursors.size() < 32) {
         this.cursors.add(cursor);
      }
   }

   public void updateCursors(final LevelAccessor level, final BlockPos originPos, final RandomSource random, final boolean spreadVeins) {
      if (!this.cursors.isEmpty()) {
         List<SculkSpreader$ChargeCursor> processedCursors = new ArrayList<>();
         Map<BlockPos, SculkSpreader$ChargeCursor> mergeableCursors = new HashMap<>();
         Object2IntMap<BlockPos> chargeMap = new Object2IntOpenHashMap();

         for (SculkSpreader$ChargeCursor cursor : this.cursors) {
            if (!cursor.isPosUnreasonable(originPos)) {
               cursor.update(level, originPos, random, this, spreadVeins);
               if (cursor.charge <= 0) {
                  level.levelEvent(3006, cursor.getPos(), 0);
               } else {
                  BlockPos pos = cursor.getPos();
                  chargeMap.computeInt(pos, (k, count) -> (count == null ? 0 : count) + cursor.charge);
                  SculkSpreader$ChargeCursor existing = mergeableCursors.get(pos);
                  if (existing == null) {
                     mergeableCursors.put(pos, cursor);
                     processedCursors.add(cursor);
                  } else if (!this.isWorldGeneration() && cursor.charge + existing.charge <= 1000) {
                     existing.mergeWith(cursor);
                  } else {
                     processedCursors.add(cursor);
                     if (cursor.charge < existing.charge) {
                        mergeableCursors.put(pos, cursor);
                     }
                  }
               }
            }
         }

         ObjectIterator var16 = chargeMap.object2IntEntrySet().iterator();

         while (var16.hasNext()) {
            Entry<BlockPos> entry = (Entry<BlockPos>)var16.next();
            BlockPos pos = (BlockPos)entry.getKey();
            int charge = entry.getIntValue();
            SculkSpreader$ChargeCursor cursorx = mergeableCursors.get(pos);
            Collection<Direction> faces = cursorx == null ? null : cursorx.getFacingData();
            if (charge > 0 && faces != null) {
               int numParticles = (int)(Math.log1p(charge) / 2.3F) + 1;
               int data = (numParticles << 6) + MultifaceBlock.pack(faces);
               level.levelEvent(3006, pos, data);
            }
         }

         this.cursors = processedCursors;
      }
   }
}
