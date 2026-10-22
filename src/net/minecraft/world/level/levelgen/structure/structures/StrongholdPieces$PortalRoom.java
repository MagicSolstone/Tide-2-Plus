package net.minecraft.world.level.levelgen.structure.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EndPortalFrameBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import org.jspecify.annotations.Nullable;

public class StrongholdPieces$PortalRoom extends StrongholdPieces$StrongholdPiece {
   protected static final int WIDTH = 11;
   protected static final int HEIGHT = 8;
   protected static final int DEPTH = 16;
   private boolean hasPlacedSpawner;

   public StrongholdPieces$PortalRoom(final int genDepth, final BoundingBox boundingBox, final Direction direction) {
      super(StructurePieceType.STRONGHOLD_PORTAL_ROOM, genDepth, boundingBox);
      this.setOrientation(direction);
   }

   public StrongholdPieces$PortalRoom(final CompoundTag tag) {
      super(StructurePieceType.STRONGHOLD_PORTAL_ROOM, tag);
      this.hasPlacedSpawner = tag.getBooleanOr("Mob", false);
   }

   @Override
   protected void addAdditionalSaveData(final StructurePieceSerializationContext context, final CompoundTag tag) {
      super.addAdditionalSaveData(context, tag);
      tag.putBoolean("Mob", this.hasPlacedSpawner);
   }

   @Override
   public void addChildren(final StructurePiece startPiece, final StructurePiecesBuilder builder, final RandomSource random) {
      if (startPiece != null) {
         ((StrongholdPieces$StartPiece)startPiece).portalRoomPiece = this;
      }
   }

   @Nullable
   public static StrongholdPieces$PortalRoom createPiece(
      final StructurePiecesBuilder builder, final int footX, final int footY, final int footZ, final Direction direction, final int genDepth
   ) {
      BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -4, -1, 0, 11, 8, 16, direction);
      return isOkBox(box) && builder.findCollisionPiece(box) == null ? new StrongholdPieces$PortalRoom(genDepth, box, direction) : null;
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
      this.generateBox(level, chunkBB, 0, 0, 0, 10, 7, 15, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
      this.generateSmallDoor(level, random, chunkBB, StrongholdPieces$StrongholdPiece$SmallDoorType.GRATES, 4, 1, 0);
      int y = 6;
      this.generateBox(level, chunkBB, 1, 6, 1, 1, 6, 14, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
      this.generateBox(level, chunkBB, 9, 6, 1, 9, 6, 14, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
      this.generateBox(level, chunkBB, 2, 6, 1, 8, 6, 2, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
      this.generateBox(level, chunkBB, 2, 6, 14, 8, 6, 14, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
      this.generateBox(level, chunkBB, 1, 1, 1, 2, 1, 4, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
      this.generateBox(level, chunkBB, 8, 1, 1, 9, 1, 4, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
      this.generateBox(level, chunkBB, 1, 1, 1, 1, 1, 3, Blocks.LAVA.defaultBlockState(), Blocks.LAVA.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 9, 1, 1, 9, 1, 3, Blocks.LAVA.defaultBlockState(), Blocks.LAVA.defaultBlockState(), false);
      this.generateBox(level, chunkBB, 3, 1, 8, 7, 1, 12, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
      this.generateBox(level, chunkBB, 4, 1, 9, 6, 1, 11, Blocks.LAVA.defaultBlockState(), Blocks.LAVA.defaultBlockState(), false);
      BlockState nsBars = Blocks.IRON_BARS.defaultBlockState().setValue(IronBarsBlock.NORTH, true).setValue(IronBarsBlock.SOUTH, true);
      BlockState weBars = Blocks.IRON_BARS.defaultBlockState().setValue(IronBarsBlock.WEST, true).setValue(IronBarsBlock.EAST, true);

      for (int z = 3; z < 14; z += 2) {
         this.generateBox(level, chunkBB, 0, 3, z, 0, 4, z, nsBars, nsBars, false);
         this.generateBox(level, chunkBB, 10, 3, z, 10, 4, z, nsBars, nsBars, false);
      }

      for (int x = 2; x < 9; x += 2) {
         this.generateBox(level, chunkBB, x, 3, 15, x, 4, 15, weBars, weBars, false);
      }

      BlockState blockState = Blocks.STONE_BRICK_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.NORTH);
      this.generateBox(level, chunkBB, 4, 1, 5, 6, 1, 7, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
      this.generateBox(level, chunkBB, 4, 2, 6, 6, 2, 7, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
      this.generateBox(level, chunkBB, 4, 3, 7, 6, 3, 7, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);

      for (int x = 4; x <= 6; x++) {
         this.placeBlock(level, blockState, x, 1, 4, chunkBB);
         this.placeBlock(level, blockState, x, 2, 5, chunkBB);
         this.placeBlock(level, blockState, x, 3, 6, chunkBB);
      }

      BlockState northFrame = Blocks.END_PORTAL_FRAME.defaultBlockState().setValue(EndPortalFrameBlock.FACING, Direction.NORTH);
      BlockState southFrame = Blocks.END_PORTAL_FRAME.defaultBlockState().setValue(EndPortalFrameBlock.FACING, Direction.SOUTH);
      BlockState eastFrame = Blocks.END_PORTAL_FRAME.defaultBlockState().setValue(EndPortalFrameBlock.FACING, Direction.EAST);
      BlockState westFrame = Blocks.END_PORTAL_FRAME.defaultBlockState().setValue(EndPortalFrameBlock.FACING, Direction.WEST);
      boolean allEyes = true;
      boolean[] eyes = new boolean[12];

      for (int i = 0; i < eyes.length; i++) {
         eyes[i] = random.nextFloat() > 0.9F;
         allEyes &= eyes[i];
      }

      this.placeBlock(level, northFrame.setValue(EndPortalFrameBlock.HAS_EYE, eyes[0]), 4, 3, 8, chunkBB);
      this.placeBlock(level, northFrame.setValue(EndPortalFrameBlock.HAS_EYE, eyes[1]), 5, 3, 8, chunkBB);
      this.placeBlock(level, northFrame.setValue(EndPortalFrameBlock.HAS_EYE, eyes[2]), 6, 3, 8, chunkBB);
      this.placeBlock(level, southFrame.setValue(EndPortalFrameBlock.HAS_EYE, eyes[3]), 4, 3, 12, chunkBB);
      this.placeBlock(level, southFrame.setValue(EndPortalFrameBlock.HAS_EYE, eyes[4]), 5, 3, 12, chunkBB);
      this.placeBlock(level, southFrame.setValue(EndPortalFrameBlock.HAS_EYE, eyes[5]), 6, 3, 12, chunkBB);
      this.placeBlock(level, eastFrame.setValue(EndPortalFrameBlock.HAS_EYE, eyes[6]), 3, 3, 9, chunkBB);
      this.placeBlock(level, eastFrame.setValue(EndPortalFrameBlock.HAS_EYE, eyes[7]), 3, 3, 10, chunkBB);
      this.placeBlock(level, eastFrame.setValue(EndPortalFrameBlock.HAS_EYE, eyes[8]), 3, 3, 11, chunkBB);
      this.placeBlock(level, westFrame.setValue(EndPortalFrameBlock.HAS_EYE, eyes[9]), 7, 3, 9, chunkBB);
      this.placeBlock(level, westFrame.setValue(EndPortalFrameBlock.HAS_EYE, eyes[10]), 7, 3, 10, chunkBB);
      this.placeBlock(level, westFrame.setValue(EndPortalFrameBlock.HAS_EYE, eyes[11]), 7, 3, 11, chunkBB);
      if (allEyes) {
         BlockState portal = Blocks.END_PORTAL.defaultBlockState();
         this.placeBlock(level, portal, 4, 3, 9, chunkBB);
         this.placeBlock(level, portal, 5, 3, 9, chunkBB);
         this.placeBlock(level, portal, 6, 3, 9, chunkBB);
         this.placeBlock(level, portal, 4, 3, 10, chunkBB);
         this.placeBlock(level, portal, 5, 3, 10, chunkBB);
         this.placeBlock(level, portal, 6, 3, 10, chunkBB);
         this.placeBlock(level, portal, 4, 3, 11, chunkBB);
         this.placeBlock(level, portal, 5, 3, 11, chunkBB);
         this.placeBlock(level, portal, 6, 3, 11, chunkBB);
      }

      if (!this.hasPlacedSpawner) {
         BlockPos pos = this.getWorldPos(5, 3, 6);
         if (chunkBB.isInside(pos)) {
            this.hasPlacedSpawner = true;
            level.setBlock(pos, Blocks.SPAWNER.defaultBlockState(), 2);
            if (level.getBlockEntity(pos) instanceof SpawnerBlockEntity spawner) {
               spawner.setEntityId(EntityTypes.SILVERFISH, random);
            }
         }
      }
   }
}
