package net.minecraft.world.level.levelgen.structure.structures;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class MineshaftPieces$MineShaftRoom extends MineshaftPieces$MineShaftPiece {
   private final List<BoundingBox> childEntranceBoxes = Lists.newLinkedList();

   public MineshaftPieces$MineShaftRoom(final int genDepth, final RandomSource random, final int west, final int north, final MineshaftStructure$Type type) {
      super(
         StructurePieceType.MINE_SHAFT_ROOM,
         genDepth,
         type,
         new BoundingBox(west, 50, north, west + 7 + random.nextInt(6), 54 + random.nextInt(6), north + 7 + random.nextInt(6))
      );
      this.type = type;
   }

   public MineshaftPieces$MineShaftRoom(final CompoundTag tag) {
      super(StructurePieceType.MINE_SHAFT_ROOM, tag);
      this.childEntranceBoxes.addAll(tag.<List<? extends BoundingBox>>read("Entrances", BoundingBox.CODEC.listOf()).orElse(List.of()));
   }

   @Override
   public void addChildren(final StructurePiece startPiece, final StructurePiecesBuilder builder, final RandomSource random) {
      int depth = this.getGenDepth();
      int heightSpace = this.boundingBox.getYSpan() - 3 - 1;
      if (heightSpace <= 0) {
         heightSpace = 1;
      }

      int pos = 0;

      while (pos < this.boundingBox.getXSpan()) {
         pos += random.nextInt(this.boundingBox.getXSpan());
         if (pos + 3 > this.boundingBox.getXSpan()) {
            break;
         }

         MineshaftPieces$MineShaftPiece child = MineshaftPieces.generateAndAddPiece(
            startPiece,
            builder,
            random,
            this.boundingBox.minX() + pos,
            this.boundingBox.minY() + random.nextInt(heightSpace) + 1,
            this.boundingBox.minZ() - 1,
            Direction.NORTH,
            depth
         );
         if (child != null) {
            BoundingBox childBox = child.getBoundingBox();
            this.childEntranceBoxes
               .add(new BoundingBox(childBox.minX(), childBox.minY(), this.boundingBox.minZ(), childBox.maxX(), childBox.maxY(), this.boundingBox.minZ() + 1));
         }

         pos += 4;
      }

      pos = 0;

      while (pos < this.boundingBox.getXSpan()) {
         pos += random.nextInt(this.boundingBox.getXSpan());
         if (pos + 3 > this.boundingBox.getXSpan()) {
            break;
         }

         MineshaftPieces$MineShaftPiece child = MineshaftPieces.generateAndAddPiece(
            startPiece,
            builder,
            random,
            this.boundingBox.minX() + pos,
            this.boundingBox.minY() + random.nextInt(heightSpace) + 1,
            this.boundingBox.maxZ() + 1,
            Direction.SOUTH,
            depth
         );
         if (child != null) {
            BoundingBox childBox = child.getBoundingBox();
            this.childEntranceBoxes
               .add(new BoundingBox(childBox.minX(), childBox.minY(), this.boundingBox.maxZ() - 1, childBox.maxX(), childBox.maxY(), this.boundingBox.maxZ()));
         }

         pos += 4;
      }

      pos = 0;

      while (pos < this.boundingBox.getZSpan()) {
         pos += random.nextInt(this.boundingBox.getZSpan());
         if (pos + 3 > this.boundingBox.getZSpan()) {
            break;
         }

         MineshaftPieces$MineShaftPiece child = MineshaftPieces.generateAndAddPiece(
            startPiece,
            builder,
            random,
            this.boundingBox.minX() - 1,
            this.boundingBox.minY() + random.nextInt(heightSpace) + 1,
            this.boundingBox.minZ() + pos,
            Direction.WEST,
            depth
         );
         if (child != null) {
            BoundingBox childBox = child.getBoundingBox();
            this.childEntranceBoxes
               .add(new BoundingBox(this.boundingBox.minX(), childBox.minY(), childBox.minZ(), this.boundingBox.minX() + 1, childBox.maxY(), childBox.maxZ()));
         }

         pos += 4;
      }

      pos = 0;

      while (pos < this.boundingBox.getZSpan()) {
         pos += random.nextInt(this.boundingBox.getZSpan());
         if (pos + 3 > this.boundingBox.getZSpan()) {
            break;
         }

         StructurePiece child = MineshaftPieces.generateAndAddPiece(
            startPiece,
            builder,
            random,
            this.boundingBox.maxX() + 1,
            this.boundingBox.minY() + random.nextInt(heightSpace) + 1,
            this.boundingBox.minZ() + pos,
            Direction.EAST,
            depth
         );
         if (child != null) {
            BoundingBox childBox = child.getBoundingBox();
            this.childEntranceBoxes
               .add(new BoundingBox(this.boundingBox.maxX() - 1, childBox.minY(), childBox.minZ(), this.boundingBox.maxX(), childBox.maxY(), childBox.maxZ()));
         }

         pos += 4;
      }
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
      if (!this.isInInvalidLocation(level, chunkBB)) {
         this.generateBox(
            level,
            chunkBB,
            this.boundingBox.minX(),
            this.boundingBox.minY() + 1,
            this.boundingBox.minZ(),
            this.boundingBox.maxX(),
            Math.min(this.boundingBox.minY() + 3, this.boundingBox.maxY()),
            this.boundingBox.maxZ(),
            CAVE_AIR,
            CAVE_AIR,
            false
         );

         for (BoundingBox entranceBox : this.childEntranceBoxes) {
            this.generateBox(
               level,
               chunkBB,
               entranceBox.minX(),
               entranceBox.maxY() - 2,
               entranceBox.minZ(),
               entranceBox.maxX(),
               entranceBox.maxY(),
               entranceBox.maxZ(),
               CAVE_AIR,
               CAVE_AIR,
               false
            );
         }

         this.generateUpperHalfSphere(
            level,
            chunkBB,
            this.boundingBox.minX(),
            this.boundingBox.minY() + 4,
            this.boundingBox.minZ(),
            this.boundingBox.maxX(),
            this.boundingBox.maxY(),
            this.boundingBox.maxZ(),
            CAVE_AIR,
            false
         );
      }
   }

   @Override
   public void move(final int dx, final int dy, final int dz) {
      super.move(dx, dy, dz);

      for (BoundingBox bb : this.childEntranceBoxes) {
         bb.move(dx, dy, dz);
      }
   }

   @Override
   protected void addAdditionalSaveData(final StructurePieceSerializationContext context, final CompoundTag tag) {
      super.addAdditionalSaveData(context, tag);
      tag.store("Entrances", BoundingBox.CODEC.listOf(), this.childEntranceBoxes);
   }
}
