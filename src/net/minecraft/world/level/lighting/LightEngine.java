package net.minecraft.world.level.lighting;

import it.unimi.dsi.fastutil.longs.LongArrayFIFOQueue;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import java.util.Arrays;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.DataLayer;
import net.minecraft.world.level.chunk.LightChunk;
import net.minecraft.world.level.chunk.LightChunkGetter;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public abstract class LightEngine<M extends DataLayerStorageMap<M>, S extends LayerLightSectionStorage<M>> implements LayerLightEventListener {
   public static final int MAX_LEVEL = 15;
   protected static final int MIN_OPACITY = 1;
   protected static final long PULL_LIGHT_IN_ENTRY = LightEngine$QueueEntry.decreaseAllDirections(1);
   private static final int MIN_QUEUE_SIZE = 512;
   protected static final Direction[] PROPAGATION_DIRECTIONS = Direction.values();
   protected final LightChunkGetter chunkSource;
   protected final S storage;
   private final LongOpenHashSet blockNodesToCheck = new LongOpenHashSet(512, 0.5F);
   private final LongArrayFIFOQueue decreaseQueue = new LongArrayFIFOQueue();
   private final LongArrayFIFOQueue increaseQueue = new LongArrayFIFOQueue();
   private static final int CACHE_SIZE = 2;
   private final long[] lastChunkPos = new long[2];
   private final LightChunk[] lastChunk = new LightChunk[2];

   protected LightEngine(final LightChunkGetter chunkSource, final S storage) {
      this.chunkSource = chunkSource;
      this.storage = storage;
      this.clearChunkCache();
   }

   public static boolean hasDifferentLightProperties(final BlockState oldState, final BlockState newState) {
      return newState == oldState
         ? false
         : newState.getLightDampening() != oldState.getLightDampening()
            || newState.getLightEmission() != oldState.getLightEmission()
            || newState.useShapeForLightOcclusion()
            || oldState.useShapeForLightOcclusion();
   }

   public static int getLightDampeningInto(final BlockState fromState, final BlockState toState, final Direction direction, final int simpleOpacity) {
      boolean fromEmpty = isEmptyShape(fromState);
      boolean toEmpty = isEmptyShape(toState);
      if (fromEmpty && toEmpty) {
         return simpleOpacity;
      } else {
         VoxelShape fromShape = fromEmpty ? Shapes.empty() : fromState.getOcclusionShape();
         VoxelShape toShape = toEmpty ? Shapes.empty() : toState.getOcclusionShape();
         return Shapes.mergedFaceOccludes(fromShape, toShape, direction) ? 16 : simpleOpacity;
      }
   }

   public static VoxelShape getOcclusionShape(final BlockState state, final Direction direction) {
      return isEmptyShape(state) ? Shapes.empty() : state.getFaceOcclusionShape(direction);
   }

   protected static boolean isEmptyShape(final BlockState state) {
      return !state.canOcclude() || !state.useShapeForLightOcclusion();
   }

   protected BlockState getState(final BlockPos pos) {
      int chunkX = SectionPos.blockToSectionCoord(pos.getX());
      int chunkZ = SectionPos.blockToSectionCoord(pos.getZ());
      LightChunk chunk = this.getChunk(chunkX, chunkZ);
      return chunk == null ? Blocks.BEDROCK.defaultBlockState() : chunk.getBlockState(pos);
   }

   protected int getOpacity(final BlockState state) {
      return Math.max(1, state.getLightDampening());
   }

   protected boolean shapeOccludes(final BlockState fromState, final BlockState toState, final Direction direction) {
      VoxelShape fromShape = getOcclusionShape(fromState, direction);
      VoxelShape toShape = getOcclusionShape(toState, direction.getOpposite());
      return Shapes.faceShapeOccludes(fromShape, toShape);
   }

   @Nullable
   protected LightChunk getChunk(final int chunkX, final int chunkZ) {
      long pos = ChunkPos.pack(chunkX, chunkZ);

      for (int i = 0; i < 2; i++) {
         if (pos == this.lastChunkPos[i]) {
            return this.lastChunk[i];
         }
      }

      LightChunk chunk = this.chunkSource.getChunkForLighting(chunkX, chunkZ);

      for (int ix = 1; ix > 0; ix--) {
         this.lastChunkPos[ix] = this.lastChunkPos[ix - 1];
         this.lastChunk[ix] = this.lastChunk[ix - 1];
      }

      this.lastChunkPos[0] = pos;
      this.lastChunk[0] = chunk;
      return chunk;
   }

   private void clearChunkCache() {
      Arrays.fill(this.lastChunkPos, ChunkPos.INVALID_CHUNK_POS);
      Arrays.fill(this.lastChunk, null);
   }

   @Override
   public void checkBlock(final BlockPos pos) {
      this.blockNodesToCheck.add(pos.asLong());
   }

   public void queueSectionData(final long pos, @Nullable final DataLayer data) {
      this.storage.queueSectionData(pos, data);
   }

   public void retainData(final ChunkPos pos, final boolean retain) {
      this.storage.retainData(SectionPos.getZeroNode(pos.x(), pos.z()), retain);
   }

   @Override
   public void updateSectionStatus(final SectionPos pos, final boolean sectionEmpty) {
      this.storage.updateSectionStatus(pos.asLong(), sectionEmpty);
   }

   @Override
   public void setLightEnabled(final ChunkPos pos, final boolean enable) {
      this.storage.setLightEnabled(SectionPos.getZeroNode(pos.x(), pos.z()), enable);
   }

   @Override
   public int runLightUpdates() {
      LongIterator iterator = this.blockNodesToCheck.iterator();

      while (iterator.hasNext()) {
         this.checkNode(iterator.nextLong());
      }

      this.blockNodesToCheck.clear();
      this.blockNodesToCheck.trim(512);
      int count = 0;
      count += this.propagateDecreases();
      count += this.propagateIncreases();
      this.clearChunkCache();
      this.storage.markNewInconsistencies(this);
      this.storage.swapSectionMap();
      return count;
   }

   private int propagateIncreases() {
      int count;
      for (count = 0; !this.increaseQueue.isEmpty(); count++) {
         long fromNode = this.increaseQueue.dequeueLong();
         long increaseData = this.increaseQueue.dequeueLong();
         int fromLevel = this.storage.getStoredLevel(fromNode);
         int fromTargetLevel = LightEngine$QueueEntry.getFromLevel(increaseData);
         if (LightEngine$QueueEntry.isIncreaseFromEmission(increaseData) && fromLevel < fromTargetLevel) {
            this.storage.setStoredLevel(fromNode, fromTargetLevel);
            fromLevel = fromTargetLevel;
         }

         if (fromLevel == fromTargetLevel) {
            this.propagateIncrease(fromNode, increaseData, fromLevel);
         }
      }

      return count;
   }

   private int propagateDecreases() {
      int count;
      for (count = 0; !this.decreaseQueue.isEmpty(); count++) {
         long fromNode = this.decreaseQueue.dequeueLong();
         long decreaseData = this.decreaseQueue.dequeueLong();
         this.propagateDecrease(fromNode, decreaseData);
      }

      return count;
   }

   protected void enqueueDecrease(final long fromNode, final long decreaseData) {
      this.decreaseQueue.enqueue(fromNode);
      this.decreaseQueue.enqueue(decreaseData);
   }

   protected void enqueueIncrease(final long fromNode, final long increaseData) {
      this.increaseQueue.enqueue(fromNode);
      this.increaseQueue.enqueue(increaseData);
   }

   @Override
   public boolean hasLightWork() {
      return this.storage.hasInconsistencies() || !this.blockNodesToCheck.isEmpty() || !this.decreaseQueue.isEmpty() || !this.increaseQueue.isEmpty();
   }

   @Nullable
   @Override
   public DataLayer getDataLayerData(final SectionPos pos) {
      return this.storage.getDataLayerData(pos.asLong());
   }

   @Override
   public int getLightValue(final BlockPos pos) {
      return this.storage.getLightValue(pos.asLong());
   }

   public String getDebugData(final long sectionNode) {
      return this.getDebugSectionType(sectionNode).display();
   }

   public LayerLightSectionStorage$SectionType getDebugSectionType(final long sectionNode) {
      return this.storage.getDebugSectionType(sectionNode);
   }

   protected abstract void checkNode(long blockNode);

   protected abstract void propagateIncrease(long fromNode, long increaseData, int fromLevel);

   protected abstract void propagateDecrease(long fromNode, long decreaseData);
}
