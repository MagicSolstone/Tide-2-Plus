package net.minecraft.world.level.chunk;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.ints.IntArrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos$MutableBlockPos;
import net.minecraft.core.CompositeDirection$Direction8;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.ticks.SavedTick;
import org.slf4j.Logger;

public class UpgradeData {
   private static final Logger LOGGER = LogUtils.getLogger();
   public static final UpgradeData EMPTY = new UpgradeData(EmptyBlockGetter.INSTANCE);
   private static final String TAG_INDICES = "Indices";
   private static final CompositeDirection$Direction8[] DIRECTIONS = CompositeDirection$Direction8.values();
   private static final Codec<List<SavedTick<Block>>> BLOCK_TICKS_CODEC = SavedTick.codec(BuiltInRegistries.BLOCK.byNameCodec().orElse(Blocks.AIR)).listOf();
   private static final Codec<List<SavedTick<Fluid>>> FLUID_TICKS_CODEC = SavedTick.codec(BuiltInRegistries.FLUID.byNameCodec().orElse(Fluids.EMPTY)).listOf();
   private final EnumSet<CompositeDirection$Direction8> sides = EnumSet.noneOf(CompositeDirection$Direction8.class);
   private final List<SavedTick<Block>> neighborBlockTicks = Lists.newArrayList();
   private final List<SavedTick<Fluid>> neighborFluidTicks = Lists.newArrayList();
   private final int[][] index;
   private static final Map<Block, UpgradeData$BlockFixer> MAP = new IdentityHashMap<>();
   private static final Set<UpgradeData$BlockFixer> CHUNKY_FIXERS = Sets.newHashSet();

   private UpgradeData(final LevelHeightAccessor levelHeightAccessor) {
      this.index = new int[levelHeightAccessor.getSectionsCount()][];
   }

   public UpgradeData(final CompoundTag tag, final LevelHeightAccessor levelHeightAccessor) {
      this(levelHeightAccessor);
      tag.getCompound("Indices").ifPresent(indicesTag -> {
         for (int i = 0; i < this.index.length; i++) {
            this.index[i] = indicesTag.getIntArray(String.valueOf(i)).orElse(null);
         }
      });
      int sideInt = tag.getIntOr("Sides", 0);

      for (CompositeDirection$Direction8 direction8 : CompositeDirection$Direction8.values()) {
         if ((sideInt & 1 << direction8.ordinal()) != 0) {
            this.sides.add(direction8);
         }
      }

      tag.<Collection<? extends E>>read("neighbor_block_ticks", BLOCK_TICKS_CODEC).ifPresent(this.neighborBlockTicks::addAll);
      tag.<Collection<? extends E>>read("neighbor_fluid_ticks", FLUID_TICKS_CODEC).ifPresent(this.neighborFluidTicks::addAll);
   }

   private UpgradeData(final UpgradeData source) {
      this.sides.addAll(source.sides);
      this.neighborBlockTicks.addAll(source.neighborBlockTicks);
      this.neighborFluidTicks.addAll(source.neighborFluidTicks);
      this.index = new int[source.index.length][];

      for (int i = 0; i < source.index.length; i++) {
         int[] indices = source.index[i];
         this.index[i] = indices != null ? IntArrays.copy(indices) : null;
      }
   }

   public void upgrade(final LevelChunk chunk) {
      this.upgradeInside(chunk);

      for (CompositeDirection$Direction8 direction8 : DIRECTIONS) {
         upgradeSides(chunk, direction8);
      }

      Level level = chunk.getLevel();
      this.neighborBlockTicks.forEach(tick -> {
         Block type = tick.type() == Blocks.AIR ? level.getBlockState(tick.pos()).getBlock() : tick.type();
         level.scheduleTick(tick.pos(), type, tick.delay(), tick.priority());
      });
      this.neighborFluidTicks.forEach(tick -> {
         Fluid type = tick.type() == Fluids.EMPTY ? level.getFluidState(tick.pos()).getType() : tick.type();
         level.scheduleTick(tick.pos(), type, tick.delay(), tick.priority());
      });
      CHUNKY_FIXERS.forEach(fixer -> fixer.processChunk(level));
   }

   private static void upgradeSides(final LevelChunk chunk, final CompositeDirection$Direction8 direction8) {
      Level level = chunk.getLevel();
      if (chunk.getUpgradeData().sides.remove(direction8)) {
         Set<Direction> directions = direction8.getDirections();
         int min = 0;
         int max = 15;
         boolean east = directions.contains(Direction.EAST);
         boolean west = directions.contains(Direction.WEST);
         boolean south = directions.contains(Direction.SOUTH);
         boolean north = directions.contains(Direction.NORTH);
         boolean singular = directions.size() == 1;
         ChunkPos chunkPos = chunk.getPos();
         int minX = chunkPos.getMinBlockX() + (!singular || !north && !south ? (west ? 0 : 15) : 1);
         int maxX = chunkPos.getMinBlockX() + (!singular || !north && !south ? (west ? 0 : 15) : 14);
         int minZ = chunkPos.getMinBlockZ() + (!singular || !east && !west ? (north ? 0 : 15) : 1);
         int maxZ = chunkPos.getMinBlockZ() + (!singular || !east && !west ? (north ? 0 : 15) : 14);
         Direction[] updateDirections = Direction.values();
         BlockPos$MutableBlockPos neighbourPos = new BlockPos$MutableBlockPos();

         for (BlockPos pos : BlockPos.betweenClosed(minX, level.getMinY(), minZ, maxX, level.getMaxY(), maxZ)) {
            BlockState state = level.getBlockState(pos);
            BlockState newState = state;

            for (Direction direction : updateDirections) {
               neighbourPos.setWithOffset(pos, direction);
               newState = updateState(newState, direction, level, pos, neighbourPos);
            }

            Block.updateOrDestroy(state, newState, level, pos, 18);
         }
      }
   }

   private static BlockState updateState(
      final BlockState state, final Direction direction, final LevelAccessor level, final BlockPos pos, final BlockPos neighbourPos
   ) {
      return MAP.getOrDefault(state.getBlock(), UpgradeData$BlockFixers.DEFAULT)
         .updateShape(state, direction, level.getBlockState(neighbourPos), level, pos, neighbourPos);
   }

   private void upgradeInside(final LevelChunk chunk) {
      BlockPos$MutableBlockPos pos = new BlockPos$MutableBlockPos();
      BlockPos$MutableBlockPos neighbourPos = new BlockPos$MutableBlockPos();
      ChunkPos chunkPos = chunk.getPos();
      LevelAccessor level = chunk.getLevel();

      for (int sectionIndex = 0; sectionIndex < this.index.length; sectionIndex++) {
         LevelChunkSection chunkSection = chunk.getSection(sectionIndex);
         int[] upgradeIndex = this.index[sectionIndex];
         this.index[sectionIndex] = null;
         if (upgradeIndex != null && upgradeIndex.length > 0) {
            Direction[] directions = Direction.values();
            PalettedContainer<BlockState> states = chunkSection.getStates();
            int sectionY = chunk.getSectionYFromSectionIndex(sectionIndex);
            int bottomYInSection = SectionPos.sectionToBlockCoord(sectionY);

            for (int coordinate : upgradeIndex) {
               int x = coordinate & 15;
               int y = coordinate >> 8 & 15;
               int z = coordinate >> 4 & 15;
               pos.set(chunkPos.getMinBlockX() + x, bottomYInSection + y, chunkPos.getMinBlockZ() + z);
               BlockState state = states.get(coordinate);
               BlockState newState = state;

               for (Direction direction : directions) {
                  neighbourPos.setWithOffset(pos, direction);
                  if (SectionPos.blockToSectionCoord(pos.getX()) == chunkPos.x() && SectionPos.blockToSectionCoord(pos.getZ()) == chunkPos.z()) {
                     newState = updateState(newState, direction, level, pos, neighbourPos);
                  }
               }

               Block.updateOrDestroy(state, newState, level, pos, 18);
            }
         }
      }

      for (int i = 0; i < this.index.length; i++) {
         if (this.index[i] != null) {
            LOGGER.warn(
               "Discarding update data for section {} for chunk ({} {})", new Object[]{level.getSectionYFromSectionIndex(i), chunkPos.x(), chunkPos.z()}
            );
         }

         this.index[i] = null;
      }
   }

   public boolean isEmpty() {
      for (int[] ints : this.index) {
         if (ints != null) {
            return false;
         }
      }

      return this.sides.isEmpty();
   }

   public CompoundTag write() {
      CompoundTag tag = new CompoundTag();
      CompoundTag indicesTag = new CompoundTag();

      for (int i = 0; i < this.index.length; i++) {
         String key = String.valueOf(i);
         if (this.index[i] != null && this.index[i].length != 0) {
            indicesTag.putIntArray(key, this.index[i]);
         }
      }

      if (!indicesTag.isEmpty()) {
         tag.put("Indices", indicesTag);
      }

      int sides = 0;

      for (CompositeDirection$Direction8 side : this.sides) {
         sides |= 1 << side.ordinal();
      }

      tag.putByte("Sides", (byte)sides);
      if (!this.neighborBlockTicks.isEmpty()) {
         tag.store("neighbor_block_ticks", BLOCK_TICKS_CODEC, this.neighborBlockTicks);
      }

      if (!this.neighborFluidTicks.isEmpty()) {
         tag.store("neighbor_fluid_ticks", FLUID_TICKS_CODEC, this.neighborFluidTicks);
      }

      return tag;
   }

   public UpgradeData copy() {
      return this == EMPTY ? EMPTY : new UpgradeData(this);
   }
}
