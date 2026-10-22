package net.minecraft.world.level.levelgen.structure.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import org.jspecify.annotations.Nullable;

public class NetherFortressPieces$BridgeEndFiller extends NetherFortressPieces$NetherBridgePiece {
   private static final int WIDTH = 5;
   private static final int HEIGHT = 10;
   private static final int DEPTH = 8;
   private final int selfSeed;

   public NetherFortressPieces$BridgeEndFiller(final int genDepth, final RandomSource random, final BoundingBox boundingBox, final Direction direction) {
      super(StructurePieceType.NETHER_FORTRESS_BRIDGE_END_FILLER, genDepth, boundingBox);
      this.setOrientation(direction);
      this.selfSeed = random.nextInt();
   }

   public NetherFortressPieces$BridgeEndFiller(final CompoundTag tag) {
      super(StructurePieceType.NETHER_FORTRESS_BRIDGE_END_FILLER, tag);
      this.selfSeed = tag.getIntOr("Seed", 0);
   }

   @Nullable
   public static NetherFortressPieces$BridgeEndFiller createPiece(
      final StructurePiecesBuilder builder,
      final RandomSource random,
      final int footX,
      final int footY,
      final int footZ,
      final Direction direction,
      final int genDepth
   ) {
      BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, -3, 0, 5, 10, 8, direction);
      return isOkBox(box) && builder.findCollisionPiece(box) == null ? new NetherFortressPieces$BridgeEndFiller(genDepth, random, box, direction) : null;
   }

   @Override
   protected void addAdditionalSaveData(final StructurePieceSerializationContext context, final CompoundTag tag) {
      super.addAdditionalSaveData(context, tag);
      tag.putInt("Seed", this.selfSeed);
   }

   @Override
   public void postProcess(
      final WorldGenLevel level,
      final StructureManager structureManager,
      final ChunkGenerator generator,
      final RandomSource random,
      final BoundingBox chunkBB,
      final ChunkPos chunkPos,
      final BlockPos referencePos
   ) {
      RandomSource selfRandom = RandomSource.createThreadLocalInstance(this.selfSeed);

      for (int x = 0; x <= 4; x++) {
         for (int y = 3; y <= 4; y++) {
            int z = selfRandom.nextInt(8);
            this.generateBox(level, chunkBB, x, y, 0, x, y, z, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
         }
      }

      int z = selfRandom.nextInt(8);
      this.generateBox(level, chunkBB, 0, 5, 0, 0, 5, z, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
      z = selfRandom.nextInt(8);
      this.generateBox(level, chunkBB, 4, 5, 0, 4, 5, z, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);

      for (int x = 0; x <= 4; x++) {
         int zx = selfRandom.nextInt(5);
         this.generateBox(level, chunkBB, x, 2, 0, x, 2, zx, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
      }

      for (int x = 0; x <= 4; x++) {
         for (int y = 0; y <= 1; y++) {
            int zx = selfRandom.nextInt(3);
            this.generateBox(level, chunkBB, x, y, 0, x, y, zx, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
         }
      }
   }
}
