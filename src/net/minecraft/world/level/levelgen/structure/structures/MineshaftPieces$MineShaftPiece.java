package net.minecraft.world.level.levelgen.structure.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos$MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

abstract class MineshaftPieces$MineShaftPiece extends StructurePiece {
   protected MineshaftStructure$Type type;

   public MineshaftPieces$MineShaftPiece(
      final StructurePieceType pieceType, final int genDepth, final MineshaftStructure$Type type, final BoundingBox boundingBox
   ) {
      super(pieceType, genDepth, boundingBox);
      this.type = type;
   }

   public MineshaftPieces$MineShaftPiece(final StructurePieceType type, final CompoundTag tag) {
      super(type, tag);
      this.type = MineshaftStructure$Type.byId(tag.getIntOr("MST", 0));
   }

   @Override
   protected boolean canBeReplaced(final LevelReader level, final int x, final int y, final int z, final BoundingBox chunkBB) {
      BlockState state = this.getBlock(level, x, y, z, chunkBB);
      return !state.is(this.type.getPlanksState().getBlock())
         && !state.is(this.type.getWoodState().getBlock())
         && !state.is(this.type.getFenceState().getBlock())
         && !state.is(Blocks.IRON_CHAIN);
   }

   @Override
   protected void addAdditionalSaveData(final StructurePieceSerializationContext context, final CompoundTag tag) {
      tag.putInt("MST", this.type.ordinal());
   }

   protected boolean isSupportingBox(final BlockGetter level, final BoundingBox chunkBB, final int x0, final int x1, final int y1, final int z0) {
      for (int x = x0; x <= x1; x++) {
         if (this.getBlock(level, x, y1 + 1, z0, chunkBB).isAir()) {
            return false;
         }
      }

      return true;
   }

   protected boolean isInInvalidLocation(final LevelAccessor level, final BoundingBox chunkBB) {
      int x0 = Math.max(this.boundingBox.minX() - 1, chunkBB.minX());
      int y0 = Math.max(this.boundingBox.minY() - 1, chunkBB.minY());
      int z0 = Math.max(this.boundingBox.minZ() - 1, chunkBB.minZ());
      int x1 = Math.min(this.boundingBox.maxX() + 1, chunkBB.maxX());
      int y1 = Math.min(this.boundingBox.maxY() + 1, chunkBB.maxY());
      int z1 = Math.min(this.boundingBox.maxZ() + 1, chunkBB.maxZ());
      BlockPos$MutableBlockPos blockPos = new BlockPos$MutableBlockPos((x0 + x1) / 2, (y0 + y1) / 2, (z0 + z1) / 2);
      if (level.getBiome(blockPos).is(BiomeTags.MINESHAFT_BLOCKING)) {
         return true;
      } else {
         for (int x = x0; x <= x1; x++) {
            for (int z = z0; z <= z1; z++) {
               if (level.getBlockState(blockPos.set(x, y0, z)).liquid()) {
                  return true;
               }

               if (level.getBlockState(blockPos.set(x, y1, z)).liquid()) {
                  return true;
               }
            }
         }

         for (int x = x0; x <= x1; x++) {
            for (int y = y0; y <= y1; y++) {
               if (level.getBlockState(blockPos.set(x, y, z0)).liquid()) {
                  return true;
               }

               if (level.getBlockState(blockPos.set(x, y, z1)).liquid()) {
                  return true;
               }
            }
         }

         for (int z = z0; z <= z1; z++) {
            for (int y = y0; y <= y1; y++) {
               if (level.getBlockState(blockPos.set(x0, y, z)).liquid()) {
                  return true;
               }

               if (level.getBlockState(blockPos.set(x1, y, z)).liquid()) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   protected void setPlanksBlock(final WorldGenLevel level, final BoundingBox chunkBB, final BlockState planksBlock, final int x, final int y, final int z) {
      if (this.isInterior(level, x, y, z, chunkBB)) {
         BlockPos pos = this.getWorldPos(x, y, z);
         BlockState existingState = level.getBlockState(pos);
         if (!existingState.isFaceSturdy(level, pos, Direction.UP)) {
            level.setBlock(pos, planksBlock, 2);
         }
      }
   }
}
