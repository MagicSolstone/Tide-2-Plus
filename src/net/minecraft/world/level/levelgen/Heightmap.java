package net.minecraft.world.level.levelgen;

import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos$MutableBlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.BitStorage;
import net.minecraft.util.Mth;
import net.minecraft.util.SimpleBitStorage;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.slf4j.Logger;

public class Heightmap {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final Predicate<BlockState> NOT_AIR = input -> !input.isAir();
   private static final Predicate<BlockState> MATERIAL_MOTION_BLOCKING = state -> state.is(BlockTags.BLOCKS_MOTION_IN_HEIGHTMAP);
   private final BitStorage data;
   private final Predicate<BlockState> isOpaque;
   private final ChunkAccess chunk;

   public Heightmap(final ChunkAccess chunk, final Heightmap$Types heightmapType) {
      this.isOpaque = heightmapType.isOpaque();
      this.chunk = chunk;
      int heightBits = Mth.ceillog2(chunk.getHeight() + 1);
      this.data = new SimpleBitStorage(heightBits, 256);
   }

   public static void primeHeightmaps(final ChunkAccess chunk, final Set<Heightmap$Types> types) {
      if (!types.isEmpty()) {
         int size = types.size();
         List<Heightmap> allHeightmaps = new ObjectArrayList(size);

         for (Heightmap$Types type : types) {
            allHeightmaps.add(chunk.getOrCreateHeightmapUnprimed(type));
         }

         List<Heightmap> remainingHeightmaps = new ObjectArrayList(size);
         int highestSectionPosition = chunk.getHighestSectionPosition() + 16;
         BlockPos$MutableBlockPos pos = new BlockPos$MutableBlockPos();

         for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
               remainingHeightmaps.clear();
               remainingHeightmaps.addAll(allHeightmaps);

               for (int y = highestSectionPosition - 1; y >= chunk.getMinY(); y--) {
                  pos.set(x, y, z);
                  BlockState state = chunk.getBlockState(pos);
                  if (!state.is(Blocks.AIR)) {
                     for (int i = 0; i < remainingHeightmaps.size(); i++) {
                        Heightmap heightmap = remainingHeightmaps.get(i);
                        if (heightmap.isOpaque.test(state)) {
                           heightmap.setHeight(x, z, y + 1);
                           remainingHeightmaps.remove(i);
                           i--;
                        }
                     }

                     if (remainingHeightmaps.isEmpty()) {
                        break;
                     }
                  }
               }
            }
         }
      }
   }

   public boolean update(final int localX, final int localY, final int localZ, final BlockState state) {
      int firstAvailable = this.getFirstAvailable(localX, localZ);
      if (localY <= firstAvailable - 2) {
         return false;
      } else {
         if (this.isOpaque.test(state)) {
            if (localY >= firstAvailable) {
               this.setHeight(localX, localZ, localY + 1);
               return true;
            }
         } else if (firstAvailable - 1 == localY) {
            BlockPos$MutableBlockPos pos = new BlockPos$MutableBlockPos();

            for (int y = localY - 1; y >= this.chunk.getMinY(); y--) {
               pos.set(localX, y, localZ);
               if (this.isOpaque.test(this.chunk.getBlockState(pos))) {
                  this.setHeight(localX, localZ, y + 1);
                  return true;
               }
            }

            this.setHeight(localX, localZ, this.chunk.getMinY());
            return true;
         }

         return false;
      }
   }

   public int getFirstAvailable(final int x, final int z) {
      return this.getFirstAvailable(getIndex(x, z));
   }

   public int getHighestTaken(final int x, final int z) {
      return this.getFirstAvailable(getIndex(x, z)) - 1;
   }

   private int getFirstAvailable(final int index) {
      return this.data.get(index) + this.chunk.getMinY();
   }

   private void setHeight(final int x, final int z, final int height) {
      this.data.set(getIndex(x, z), height - this.chunk.getMinY());
   }

   public void setRawData(final ChunkAccess chunk, final Heightmap$Types type, final long[] data) {
      long[] rawData = this.data.getRaw();
      if (rawData.length == data.length) {
         System.arraycopy(data, 0, rawData, 0, data.length);
      } else {
         LOGGER.warn(
            "Ignoring heightmap data for chunk {}, size does not match; expected: {}, got: {}", new Object[]{chunk.getPos(), rawData.length, data.length}
         );
         primeHeightmaps(chunk, EnumSet.of(type));
      }
   }

   public long[] getRawData() {
      return this.data.getRaw();
   }

   private static int getIndex(final int x, final int z) {
      return x + z * 16;
   }
}
