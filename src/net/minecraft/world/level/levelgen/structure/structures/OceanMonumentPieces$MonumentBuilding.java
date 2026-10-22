package net.minecraft.world.level.levelgen.structure.structures;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public class OceanMonumentPieces$MonumentBuilding extends OceanMonumentPieces$OceanMonumentPiece {
   private static final int WIDTH = 58;
   private static final int HEIGHT = 22;
   private static final int DEPTH = 58;
   public static final int BIOME_RANGE_CHECK = 29;
   private static final int TOP_POSITION = 61;
   private OceanMonumentPieces$RoomDefinition sourceRoom;
   private OceanMonumentPieces$RoomDefinition coreRoom;
   private final List<OceanMonumentPieces$OceanMonumentPiece> childPieces = Lists.newArrayList();

   public OceanMonumentPieces$MonumentBuilding(final RandomSource random, final int west, final int north, final Direction direction) {
      super(StructurePieceType.OCEAN_MONUMENT_BUILDING, direction, 0, makeBoundingBox(west, 39, north, direction, 58, 23, 58));
      this.setOrientation(direction);
      List<OceanMonumentPieces$RoomDefinition> roomDefinitions = this.generateRoomGraph(random);
      this.sourceRoom.claimed = true;
      this.childPieces.add(new OceanMonumentPieces$OceanMonumentEntryRoom(direction, this.sourceRoom));
      this.childPieces.add(new OceanMonumentPieces$OceanMonumentCoreRoom(direction, this.coreRoom));
      List<OceanMonumentPieces$MonumentRoomFitter> fitters = Lists.newArrayList();
      fitters.add(new OceanMonumentPieces$FitDoubleXYRoom());
      fitters.add(new OceanMonumentPieces$FitDoubleYZRoom());
      fitters.add(new OceanMonumentPieces$FitDoubleZRoom());
      fitters.add(new OceanMonumentPieces$FitDoubleXRoom());
      fitters.add(new OceanMonumentPieces$FitDoubleYRoom());
      fitters.add(new OceanMonumentPieces$FitSimpleTopRoom());
      fitters.add(new OceanMonumentPieces$FitSimpleRoom());

      for (OceanMonumentPieces$RoomDefinition definition : roomDefinitions) {
         if (!definition.claimed && !definition.isSpecial()) {
            for (OceanMonumentPieces$MonumentRoomFitter fitter : fitters) {
               if (fitter.fits(definition)) {
                  this.childPieces.add(fitter.create(direction, definition, random));
                  break;
               }
            }
         }
      }

      BlockPos offset = this.getWorldPos(9, 0, 22);

      for (OceanMonumentPieces$OceanMonumentPiece child : this.childPieces) {
         child.getBoundingBox().move(offset);
      }

      BoundingBox leftWing = BoundingBox.fromCorners(this.getWorldPos(1, 1, 1), this.getWorldPos(23, 8, 21));
      BoundingBox rightWing = BoundingBox.fromCorners(this.getWorldPos(34, 1, 1), this.getWorldPos(56, 8, 21));
      BoundingBox penthouse = BoundingBox.fromCorners(this.getWorldPos(22, 13, 22), this.getWorldPos(35, 17, 35));
      int wingRandom = random.nextInt();
      this.childPieces.add(new OceanMonumentPieces$OceanMonumentWingRoom(direction, leftWing, wingRandom++));
      this.childPieces.add(new OceanMonumentPieces$OceanMonumentWingRoom(direction, rightWing, wingRandom++));
      this.childPieces.add(new OceanMonumentPieces$OceanMonumentPenthouse(direction, penthouse));
   }

   public OceanMonumentPieces$MonumentBuilding(final CompoundTag tag) {
      super(StructurePieceType.OCEAN_MONUMENT_BUILDING, tag);
   }

   private List<OceanMonumentPieces$RoomDefinition> generateRoomGraph(final RandomSource random) {
      OceanMonumentPieces$RoomDefinition[] roomGrid = new OceanMonumentPieces$RoomDefinition[75];

      for (int x = 0; x < 5; x++) {
         for (int z = 0; z < 4; z++) {
            int y = 0;
            int pos = getRoomIndex(x, 0, z);
            roomGrid[pos] = new OceanMonumentPieces$RoomDefinition(pos);
         }
      }

      for (int x = 0; x < 5; x++) {
         for (int z = 0; z < 4; z++) {
            int y = 1;
            int pos = getRoomIndex(x, 1, z);
            roomGrid[pos] = new OceanMonumentPieces$RoomDefinition(pos);
         }
      }

      for (int x = 1; x < 4; x++) {
         for (int z = 0; z < 2; z++) {
            int y = 2;
            int pos = getRoomIndex(x, 2, z);
            roomGrid[pos] = new OceanMonumentPieces$RoomDefinition(pos);
         }
      }

      this.sourceRoom = roomGrid[GRIDROOM_SOURCE_INDEX];

      for (int x = 0; x < 5; x++) {
         for (int z = 0; z < 5; z++) {
            for (int y = 0; y < 3; y++) {
               int pos = getRoomIndex(x, y, z);
               if (roomGrid[pos] != null) {
                  for (Direction direction : Direction.values()) {
                     int neighX = x + direction.getStepX();
                     int neighY = y + direction.getStepY();
                     int neighZ = z + direction.getStepZ();
                     if (neighX >= 0 && neighX < 5 && neighZ >= 0 && neighZ < 5 && neighY >= 0 && neighY < 3) {
                        int neighPos = getRoomIndex(neighX, neighY, neighZ);
                        if (roomGrid[neighPos] != null) {
                           if (neighZ == z) {
                              roomGrid[pos].setConnection(direction, roomGrid[neighPos]);
                           } else {
                              roomGrid[pos].setConnection(direction.getOpposite(), roomGrid[neighPos]);
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      OceanMonumentPieces$RoomDefinition roofRoom = new OceanMonumentPieces$RoomDefinition(1003);
      OceanMonumentPieces$RoomDefinition leftWing = new OceanMonumentPieces$RoomDefinition(1001);
      OceanMonumentPieces$RoomDefinition rightWing = new OceanMonumentPieces$RoomDefinition(1002);
      roomGrid[GRIDROOM_TOP_CONNECT_INDEX].setConnection(Direction.UP, roofRoom);
      roomGrid[GRIDROOM_LEFTWING_CONNECT_INDEX].setConnection(Direction.SOUTH, leftWing);
      roomGrid[GRIDROOM_RIGHTWING_CONNECT_INDEX].setConnection(Direction.SOUTH, rightWing);
      roofRoom.claimed = true;
      leftWing.claimed = true;
      rightWing.claimed = true;
      this.sourceRoom.isSource = true;
      this.coreRoom = roomGrid[getRoomIndex(random.nextInt(4), 0, 2)];
      this.coreRoom.claimed = true;
      this.coreRoom.connections[Direction.EAST.get3DDataValue()].claimed = true;
      this.coreRoom.connections[Direction.NORTH.get3DDataValue()].claimed = true;
      this.coreRoom.connections[Direction.EAST.get3DDataValue()].connections[Direction.NORTH.get3DDataValue()].claimed = true;
      this.coreRoom.connections[Direction.UP.get3DDataValue()].claimed = true;
      this.coreRoom.connections[Direction.EAST.get3DDataValue()].connections[Direction.UP.get3DDataValue()].claimed = true;
      this.coreRoom.connections[Direction.NORTH.get3DDataValue()].connections[Direction.UP.get3DDataValue()].claimed = true;
      this.coreRoom.connections[Direction.EAST.get3DDataValue()].connections[Direction.NORTH.get3DDataValue()].connections[Direction.UP.get3DDataValue()].claimed = true;
      ObjectArrayList<OceanMonumentPieces$RoomDefinition> roomDefs = new ObjectArrayList();

      for (OceanMonumentPieces$RoomDefinition definition : roomGrid) {
         if (definition != null) {
            definition.updateOpenings();
            roomDefs.add(definition);
         }
      }

      roofRoom.updateOpenings();
      Util.shuffle(roomDefs, random);
      int scanIndex = 1;
      ObjectListIterator var34 = roomDefs.iterator();

      while (var34.hasNext()) {
         OceanMonumentPieces$RoomDefinition definitionx = (OceanMonumentPieces$RoomDefinition)var34.next();
         int closeCount = 0;
         int attemptCount = 0;

         while (closeCount < 2 && attemptCount < 5) {
            attemptCount++;
            int f = random.nextInt(6);
            if (definitionx.hasOpening[f]) {
               int of = Direction.from3DDataValue(f).getOpposite().get3DDataValue();
               definitionx.hasOpening[f] = false;
               definitionx.connections[f].hasOpening[of] = false;
               if (definitionx.findSource(scanIndex++) && definitionx.connections[f].findSource(scanIndex++)) {
                  closeCount++;
               } else {
                  definitionx.hasOpening[f] = true;
                  definitionx.connections[f].hasOpening[of] = true;
               }
            }
         }
      }

      roomDefs.add(roofRoom);
      roomDefs.add(leftWing);
      roomDefs.add(rightWing);
      return roomDefs;
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
      int waterHeight = Math.max(level.getSeaLevel(), 64) - this.boundingBox.minY();
      this.generateWaterBox(level, chunkBB, 0, 0, 0, 58, waterHeight, 58);
      this.generateWing(false, 0, level, random, chunkBB);
      this.generateWing(true, 33, level, random, chunkBB);
      this.generateEntranceArchs(level, random, chunkBB);
      this.generateEntranceWall(level, random, chunkBB);
      this.generateRoofPiece(level, random, chunkBB);
      this.generateLowerWall(level, random, chunkBB);
      this.generateMiddleWall(level, random, chunkBB);
      this.generateUpperWall(level, random, chunkBB);

      for (int pillarX = 0; pillarX < 7; pillarX++) {
         int pillarZ = 0;

         while (pillarZ < 7) {
            if (pillarZ == 0 && pillarX == 3) {
               pillarZ = 6;
            }

            int bx = pillarX * 9;
            int bz = pillarZ * 9;

            for (int w = 0; w < 4; w++) {
               for (int d = 0; d < 4; d++) {
                  this.placeBlock(level, BASE_LIGHT, bx + w, 0, bz + d, chunkBB);
                  this.fillColumnDown(level, BASE_LIGHT, bx + w, -1, bz + d, chunkBB);
               }
            }

            if (pillarX != 0 && pillarX != 6) {
               pillarZ += 6;
            } else {
               pillarZ++;
            }
         }
      }

      for (int i = 0; i < 5; i++) {
         this.generateWaterBox(level, chunkBB, -1 - i, 0 + i * 2, -1 - i, -1 - i, 23, 58 + i);
         this.generateWaterBox(level, chunkBB, 58 + i, 0 + i * 2, -1 - i, 58 + i, 23, 58 + i);
         this.generateWaterBox(level, chunkBB, 0 - i, 0 + i * 2, -1 - i, 57 + i, 23, -1 - i);
         this.generateWaterBox(level, chunkBB, 0 - i, 0 + i * 2, 58 + i, 57 + i, 23, 58 + i);
      }

      for (OceanMonumentPieces$OceanMonumentPiece child : this.childPieces) {
         if (child.getBoundingBox().intersects(chunkBB)) {
            child.postProcess(level, structureManager, generator, random, chunkBB, chunkPos, referencePos);
         }
      }
   }

   private void generateWing(final boolean isFlipped, final int xoff, final WorldGenLevel level, final RandomSource random, final BoundingBox chunkBB) {
      int sectionWidth = 24;
      if (this.chunkIntersects(chunkBB, xoff, 0, xoff + 23, 20)) {
         this.generateBox(level, chunkBB, xoff + 0, 0, 0, xoff + 24, 0, 20, BASE_GRAY, BASE_GRAY, false);
         this.generateWaterBox(level, chunkBB, xoff + 0, 1, 0, xoff + 24, 10, 20);

         for (int i = 0; i < 4; i++) {
            this.generateBox(level, chunkBB, xoff + i, i + 1, i, xoff + i, i + 1, 20, BASE_LIGHT, BASE_LIGHT, false);
            this.generateBox(level, chunkBB, xoff + i + 7, i + 5, i + 7, xoff + i + 7, i + 5, 20, BASE_LIGHT, BASE_LIGHT, false);
            this.generateBox(level, chunkBB, xoff + 17 - i, i + 5, i + 7, xoff + 17 - i, i + 5, 20, BASE_LIGHT, BASE_LIGHT, false);
            this.generateBox(level, chunkBB, xoff + 24 - i, i + 1, i, xoff + 24 - i, i + 1, 20, BASE_LIGHT, BASE_LIGHT, false);
            this.generateBox(level, chunkBB, xoff + i + 1, i + 1, i, xoff + 23 - i, i + 1, i, BASE_LIGHT, BASE_LIGHT, false);
            this.generateBox(level, chunkBB, xoff + i + 8, i + 5, i + 7, xoff + 16 - i, i + 5, i + 7, BASE_LIGHT, BASE_LIGHT, false);
         }

         this.generateBox(level, chunkBB, xoff + 4, 4, 4, xoff + 6, 4, 20, BASE_GRAY, BASE_GRAY, false);
         this.generateBox(level, chunkBB, xoff + 7, 4, 4, xoff + 17, 4, 6, BASE_GRAY, BASE_GRAY, false);
         this.generateBox(level, chunkBB, xoff + 18, 4, 4, xoff + 20, 4, 20, BASE_GRAY, BASE_GRAY, false);
         this.generateBox(level, chunkBB, xoff + 11, 8, 11, xoff + 13, 8, 20, BASE_GRAY, BASE_GRAY, false);
         this.placeBlock(level, DOT_DECO_DATA, xoff + 12, 9, 12, chunkBB);
         this.placeBlock(level, DOT_DECO_DATA, xoff + 12, 9, 15, chunkBB);
         this.placeBlock(level, DOT_DECO_DATA, xoff + 12, 9, 18, chunkBB);
         int leftPos = xoff + (isFlipped ? 19 : 5);
         int rightPos = xoff + (isFlipped ? 5 : 19);

         for (int z = 20; z >= 5; z -= 3) {
            this.placeBlock(level, DOT_DECO_DATA, leftPos, 5, z, chunkBB);
         }

         for (int z = 19; z >= 7; z -= 3) {
            this.placeBlock(level, DOT_DECO_DATA, rightPos, 5, z, chunkBB);
         }

         for (int i = 0; i < 4; i++) {
            int pos = isFlipped ? xoff + 24 - (17 - i * 3) : xoff + 17 - i * 3;
            this.placeBlock(level, DOT_DECO_DATA, pos, 5, 5, chunkBB);
         }

         this.placeBlock(level, DOT_DECO_DATA, rightPos, 5, 5, chunkBB);
         this.generateBox(level, chunkBB, xoff + 11, 1, 12, xoff + 13, 7, 12, BASE_GRAY, BASE_GRAY, false);
         this.generateBox(level, chunkBB, xoff + 12, 1, 11, xoff + 12, 7, 13, BASE_GRAY, BASE_GRAY, false);
      }
   }

   private void generateEntranceArchs(final WorldGenLevel level, final RandomSource random, final BoundingBox chunkBB) {
      if (this.chunkIntersects(chunkBB, 22, 5, 35, 17)) {
         this.generateWaterBox(level, chunkBB, 25, 0, 0, 32, 8, 20);

         for (int i = 0; i < 4; i++) {
            this.generateBox(level, chunkBB, 24, 2, 5 + i * 4, 24, 4, 5 + i * 4, BASE_LIGHT, BASE_LIGHT, false);
            this.generateBox(level, chunkBB, 22, 4, 5 + i * 4, 23, 4, 5 + i * 4, BASE_LIGHT, BASE_LIGHT, false);
            this.placeBlock(level, BASE_LIGHT, 25, 5, 5 + i * 4, chunkBB);
            this.placeBlock(level, BASE_LIGHT, 26, 6, 5 + i * 4, chunkBB);
            this.placeBlock(level, LAMP_BLOCK, 26, 5, 5 + i * 4, chunkBB);
            this.generateBox(level, chunkBB, 33, 2, 5 + i * 4, 33, 4, 5 + i * 4, BASE_LIGHT, BASE_LIGHT, false);
            this.generateBox(level, chunkBB, 34, 4, 5 + i * 4, 35, 4, 5 + i * 4, BASE_LIGHT, BASE_LIGHT, false);
            this.placeBlock(level, BASE_LIGHT, 32, 5, 5 + i * 4, chunkBB);
            this.placeBlock(level, BASE_LIGHT, 31, 6, 5 + i * 4, chunkBB);
            this.placeBlock(level, LAMP_BLOCK, 31, 5, 5 + i * 4, chunkBB);
            this.generateBox(level, chunkBB, 27, 6, 5 + i * 4, 30, 6, 5 + i * 4, BASE_GRAY, BASE_GRAY, false);
         }
      }
   }

   private void generateEntranceWall(final WorldGenLevel level, final RandomSource random, final BoundingBox chunkBB) {
      if (this.chunkIntersects(chunkBB, 15, 20, 42, 21)) {
         this.generateBox(level, chunkBB, 15, 0, 21, 42, 0, 21, BASE_GRAY, BASE_GRAY, false);
         this.generateWaterBox(level, chunkBB, 26, 1, 21, 31, 3, 21);
         this.generateBox(level, chunkBB, 21, 12, 21, 36, 12, 21, BASE_GRAY, BASE_GRAY, false);
         this.generateBox(level, chunkBB, 17, 11, 21, 40, 11, 21, BASE_GRAY, BASE_GRAY, false);
         this.generateBox(level, chunkBB, 16, 10, 21, 41, 10, 21, BASE_GRAY, BASE_GRAY, false);
         this.generateBox(level, chunkBB, 15, 7, 21, 42, 9, 21, BASE_GRAY, BASE_GRAY, false);
         this.generateBox(level, chunkBB, 16, 6, 21, 41, 6, 21, BASE_GRAY, BASE_GRAY, false);
         this.generateBox(level, chunkBB, 17, 5, 21, 40, 5, 21, BASE_GRAY, BASE_GRAY, false);
         this.generateBox(level, chunkBB, 21, 4, 21, 36, 4, 21, BASE_GRAY, BASE_GRAY, false);
         this.generateBox(level, chunkBB, 22, 3, 21, 26, 3, 21, BASE_GRAY, BASE_GRAY, false);
         this.generateBox(level, chunkBB, 31, 3, 21, 35, 3, 21, BASE_GRAY, BASE_GRAY, false);
         this.generateBox(level, chunkBB, 23, 2, 21, 25, 2, 21, BASE_GRAY, BASE_GRAY, false);
         this.generateBox(level, chunkBB, 32, 2, 21, 34, 2, 21, BASE_GRAY, BASE_GRAY, false);
         this.generateBox(level, chunkBB, 28, 4, 20, 29, 4, 21, BASE_LIGHT, BASE_LIGHT, false);
         this.placeBlock(level, BASE_LIGHT, 27, 3, 21, chunkBB);
         this.placeBlock(level, BASE_LIGHT, 30, 3, 21, chunkBB);
         this.placeBlock(level, BASE_LIGHT, 26, 2, 21, chunkBB);
         this.placeBlock(level, BASE_LIGHT, 31, 2, 21, chunkBB);
         this.placeBlock(level, BASE_LIGHT, 25, 1, 21, chunkBB);
         this.placeBlock(level, BASE_LIGHT, 32, 1, 21, chunkBB);

         for (int i = 0; i < 7; i++) {
            this.placeBlock(level, BASE_BLACK, 28 - i, 6 + i, 21, chunkBB);
            this.placeBlock(level, BASE_BLACK, 29 + i, 6 + i, 21, chunkBB);
         }

         for (int i = 0; i < 4; i++) {
            this.placeBlock(level, BASE_BLACK, 28 - i, 9 + i, 21, chunkBB);
            this.placeBlock(level, BASE_BLACK, 29 + i, 9 + i, 21, chunkBB);
         }

         this.placeBlock(level, BASE_BLACK, 28, 12, 21, chunkBB);
         this.placeBlock(level, BASE_BLACK, 29, 12, 21, chunkBB);

         for (int i = 0; i < 3; i++) {
            this.placeBlock(level, BASE_BLACK, 22 - i * 2, 8, 21, chunkBB);
            this.placeBlock(level, BASE_BLACK, 22 - i * 2, 9, 21, chunkBB);
            this.placeBlock(level, BASE_BLACK, 35 + i * 2, 8, 21, chunkBB);
            this.placeBlock(level, BASE_BLACK, 35 + i * 2, 9, 21, chunkBB);
         }

         this.generateWaterBox(level, chunkBB, 15, 13, 21, 42, 15, 21);
         this.generateWaterBox(level, chunkBB, 15, 1, 21, 15, 6, 21);
         this.generateWaterBox(level, chunkBB, 16, 1, 21, 16, 5, 21);
         this.generateWaterBox(level, chunkBB, 17, 1, 21, 20, 4, 21);
         this.generateWaterBox(level, chunkBB, 21, 1, 21, 21, 3, 21);
         this.generateWaterBox(level, chunkBB, 22, 1, 21, 22, 2, 21);
         this.generateWaterBox(level, chunkBB, 23, 1, 21, 24, 1, 21);
         this.generateWaterBox(level, chunkBB, 42, 1, 21, 42, 6, 21);
         this.generateWaterBox(level, chunkBB, 41, 1, 21, 41, 5, 21);
         this.generateWaterBox(level, chunkBB, 37, 1, 21, 40, 4, 21);
         this.generateWaterBox(level, chunkBB, 36, 1, 21, 36, 3, 21);
         this.generateWaterBox(level, chunkBB, 33, 1, 21, 34, 1, 21);
         this.generateWaterBox(level, chunkBB, 35, 1, 21, 35, 2, 21);
      }
   }

   private void generateRoofPiece(final WorldGenLevel level, final RandomSource random, final BoundingBox chunkBB) {
      if (this.chunkIntersects(chunkBB, 21, 21, 36, 36)) {
         this.generateBox(level, chunkBB, 21, 0, 22, 36, 0, 36, BASE_GRAY, BASE_GRAY, false);
         this.generateWaterBox(level, chunkBB, 21, 1, 22, 36, 23, 36);

         for (int i = 0; i < 4; i++) {
            this.generateBox(level, chunkBB, 21 + i, 13 + i, 21 + i, 36 - i, 13 + i, 21 + i, BASE_LIGHT, BASE_LIGHT, false);
            this.generateBox(level, chunkBB, 21 + i, 13 + i, 36 - i, 36 - i, 13 + i, 36 - i, BASE_LIGHT, BASE_LIGHT, false);
            this.generateBox(level, chunkBB, 21 + i, 13 + i, 22 + i, 21 + i, 13 + i, 35 - i, BASE_LIGHT, BASE_LIGHT, false);
            this.generateBox(level, chunkBB, 36 - i, 13 + i, 22 + i, 36 - i, 13 + i, 35 - i, BASE_LIGHT, BASE_LIGHT, false);
         }

         this.generateBox(level, chunkBB, 25, 16, 25, 32, 16, 32, BASE_GRAY, BASE_GRAY, false);
         this.generateBox(level, chunkBB, 25, 17, 25, 25, 19, 25, BASE_LIGHT, BASE_LIGHT, false);
         this.generateBox(level, chunkBB, 32, 17, 25, 32, 19, 25, BASE_LIGHT, BASE_LIGHT, false);
         this.generateBox(level, chunkBB, 25, 17, 32, 25, 19, 32, BASE_LIGHT, BASE_LIGHT, false);
         this.generateBox(level, chunkBB, 32, 17, 32, 32, 19, 32, BASE_LIGHT, BASE_LIGHT, false);
         this.placeBlock(level, BASE_LIGHT, 26, 20, 26, chunkBB);
         this.placeBlock(level, BASE_LIGHT, 27, 21, 27, chunkBB);
         this.placeBlock(level, LAMP_BLOCK, 27, 20, 27, chunkBB);
         this.placeBlock(level, BASE_LIGHT, 26, 20, 31, chunkBB);
         this.placeBlock(level, BASE_LIGHT, 27, 21, 30, chunkBB);
         this.placeBlock(level, LAMP_BLOCK, 27, 20, 30, chunkBB);
         this.placeBlock(level, BASE_LIGHT, 31, 20, 31, chunkBB);
         this.placeBlock(level, BASE_LIGHT, 30, 21, 30, chunkBB);
         this.placeBlock(level, LAMP_BLOCK, 30, 20, 30, chunkBB);
         this.placeBlock(level, BASE_LIGHT, 31, 20, 26, chunkBB);
         this.placeBlock(level, BASE_LIGHT, 30, 21, 27, chunkBB);
         this.placeBlock(level, LAMP_BLOCK, 30, 20, 27, chunkBB);
         this.generateBox(level, chunkBB, 28, 21, 27, 29, 21, 27, BASE_GRAY, BASE_GRAY, false);
         this.generateBox(level, chunkBB, 27, 21, 28, 27, 21, 29, BASE_GRAY, BASE_GRAY, false);
         this.generateBox(level, chunkBB, 28, 21, 30, 29, 21, 30, BASE_GRAY, BASE_GRAY, false);
         this.generateBox(level, chunkBB, 30, 21, 28, 30, 21, 29, BASE_GRAY, BASE_GRAY, false);
      }
   }

   private void generateLowerWall(final WorldGenLevel level, final RandomSource random, final BoundingBox chunkBB) {
      if (this.chunkIntersects(chunkBB, 0, 21, 6, 58)) {
         this.generateBox(level, chunkBB, 0, 0, 21, 6, 0, 57, BASE_GRAY, BASE_GRAY, false);
         this.generateWaterBox(level, chunkBB, 0, 1, 21, 6, 7, 57);
         this.generateBox(level, chunkBB, 4, 4, 21, 6, 4, 53, BASE_GRAY, BASE_GRAY, false);

         for (int i = 0; i < 4; i++) {
            this.generateBox(level, chunkBB, i, i + 1, 21, i, i + 1, 57 - i, BASE_LIGHT, BASE_LIGHT, false);
         }

         for (int z = 23; z < 53; z += 3) {
            this.placeBlock(level, DOT_DECO_DATA, 5, 5, z, chunkBB);
         }

         this.placeBlock(level, DOT_DECO_DATA, 5, 5, 52, chunkBB);

         for (int i = 0; i < 4; i++) {
            this.generateBox(level, chunkBB, i, i + 1, 21, i, i + 1, 57 - i, BASE_LIGHT, BASE_LIGHT, false);
         }

         this.generateBox(level, chunkBB, 4, 1, 52, 6, 3, 52, BASE_GRAY, BASE_GRAY, false);
         this.generateBox(level, chunkBB, 5, 1, 51, 5, 3, 53, BASE_GRAY, BASE_GRAY, false);
      }

      if (this.chunkIntersects(chunkBB, 51, 21, 58, 58)) {
         this.generateBox(level, chunkBB, 51, 0, 21, 57, 0, 57, BASE_GRAY, BASE_GRAY, false);
         this.generateWaterBox(level, chunkBB, 51, 1, 21, 57, 7, 57);
         this.generateBox(level, chunkBB, 51, 4, 21, 53, 4, 53, BASE_GRAY, BASE_GRAY, false);

         for (int i = 0; i < 4; i++) {
            this.generateBox(level, chunkBB, 57 - i, i + 1, 21, 57 - i, i + 1, 57 - i, BASE_LIGHT, BASE_LIGHT, false);
         }

         for (int z = 23; z < 53; z += 3) {
            this.placeBlock(level, DOT_DECO_DATA, 52, 5, z, chunkBB);
         }

         this.placeBlock(level, DOT_DECO_DATA, 52, 5, 52, chunkBB);
         this.generateBox(level, chunkBB, 51, 1, 52, 53, 3, 52, BASE_GRAY, BASE_GRAY, false);
         this.generateBox(level, chunkBB, 52, 1, 51, 52, 3, 53, BASE_GRAY, BASE_GRAY, false);
      }

      if (this.chunkIntersects(chunkBB, 0, 51, 57, 57)) {
         this.generateBox(level, chunkBB, 7, 0, 51, 50, 0, 57, BASE_GRAY, BASE_GRAY, false);
         this.generateWaterBox(level, chunkBB, 7, 1, 51, 50, 10, 57);

         for (int i = 0; i < 4; i++) {
            this.generateBox(level, chunkBB, i + 1, i + 1, 57 - i, 56 - i, i + 1, 57 - i, BASE_LIGHT, BASE_LIGHT, false);
         }
      }
   }

   private void generateMiddleWall(final WorldGenLevel level, final RandomSource random, final BoundingBox chunkBB) {
      if (this.chunkIntersects(chunkBB, 7, 21, 13, 50)) {
         this.generateBox(level, chunkBB, 7, 0, 21, 13, 0, 50, BASE_GRAY, BASE_GRAY, false);
         this.generateWaterBox(level, chunkBB, 7, 1, 21, 13, 10, 50);
         this.generateBox(level, chunkBB, 11, 8, 21, 13, 8, 53, BASE_GRAY, BASE_GRAY, false);

         for (int i = 0; i < 4; i++) {
            this.generateBox(level, chunkBB, i + 7, i + 5, 21, i + 7, i + 5, 54, BASE_LIGHT, BASE_LIGHT, false);
         }

         for (int z = 21; z <= 45; z += 3) {
            this.placeBlock(level, DOT_DECO_DATA, 12, 9, z, chunkBB);
         }
      }

      if (this.chunkIntersects(chunkBB, 44, 21, 50, 54)) {
         this.generateBox(level, chunkBB, 44, 0, 21, 50, 0, 50, BASE_GRAY, BASE_GRAY, false);
         this.generateWaterBox(level, chunkBB, 44, 1, 21, 50, 10, 50);
         this.generateBox(level, chunkBB, 44, 8, 21, 46, 8, 53, BASE_GRAY, BASE_GRAY, false);

         for (int i = 0; i < 4; i++) {
            this.generateBox(level, chunkBB, 50 - i, i + 5, 21, 50 - i, i + 5, 54, BASE_LIGHT, BASE_LIGHT, false);
         }

         for (int z = 21; z <= 45; z += 3) {
            this.placeBlock(level, DOT_DECO_DATA, 45, 9, z, chunkBB);
         }
      }

      if (this.chunkIntersects(chunkBB, 8, 44, 49, 54)) {
         this.generateBox(level, chunkBB, 14, 0, 44, 43, 0, 50, BASE_GRAY, BASE_GRAY, false);
         this.generateWaterBox(level, chunkBB, 14, 1, 44, 43, 10, 50);

         for (int x = 12; x <= 45; x += 3) {
            this.placeBlock(level, DOT_DECO_DATA, x, 9, 45, chunkBB);
            this.placeBlock(level, DOT_DECO_DATA, x, 9, 52, chunkBB);
            if (x == 12 || x == 18 || x == 24 || x == 33 || x == 39 || x == 45) {
               this.placeBlock(level, DOT_DECO_DATA, x, 9, 47, chunkBB);
               this.placeBlock(level, DOT_DECO_DATA, x, 9, 50, chunkBB);
               this.placeBlock(level, DOT_DECO_DATA, x, 10, 45, chunkBB);
               this.placeBlock(level, DOT_DECO_DATA, x, 10, 46, chunkBB);
               this.placeBlock(level, DOT_DECO_DATA, x, 10, 51, chunkBB);
               this.placeBlock(level, DOT_DECO_DATA, x, 10, 52, chunkBB);
               this.placeBlock(level, DOT_DECO_DATA, x, 11, 47, chunkBB);
               this.placeBlock(level, DOT_DECO_DATA, x, 11, 50, chunkBB);
               this.placeBlock(level, DOT_DECO_DATA, x, 12, 48, chunkBB);
               this.placeBlock(level, DOT_DECO_DATA, x, 12, 49, chunkBB);
            }
         }

         for (int i = 0; i < 3; i++) {
            this.generateBox(level, chunkBB, 8 + i, 5 + i, 54, 49 - i, 5 + i, 54, BASE_GRAY, BASE_GRAY, false);
         }

         this.generateBox(level, chunkBB, 11, 8, 54, 46, 8, 54, BASE_LIGHT, BASE_LIGHT, false);
         this.generateBox(level, chunkBB, 14, 8, 44, 43, 8, 53, BASE_GRAY, BASE_GRAY, false);
      }
   }

   private void generateUpperWall(final WorldGenLevel level, final RandomSource random, final BoundingBox chunkBB) {
      if (this.chunkIntersects(chunkBB, 14, 21, 20, 43)) {
         this.generateBox(level, chunkBB, 14, 0, 21, 20, 0, 43, BASE_GRAY, BASE_GRAY, false);
         this.generateWaterBox(level, chunkBB, 14, 1, 22, 20, 14, 43);
         this.generateBox(level, chunkBB, 18, 12, 22, 20, 12, 39, BASE_GRAY, BASE_GRAY, false);
         this.generateBox(level, chunkBB, 18, 12, 21, 20, 12, 21, BASE_LIGHT, BASE_LIGHT, false);

         for (int i = 0; i < 4; i++) {
            this.generateBox(level, chunkBB, i + 14, i + 9, 21, i + 14, i + 9, 43 - i, BASE_LIGHT, BASE_LIGHT, false);
         }

         for (int z = 23; z <= 39; z += 3) {
            this.placeBlock(level, DOT_DECO_DATA, 19, 13, z, chunkBB);
         }
      }

      if (this.chunkIntersects(chunkBB, 37, 21, 43, 43)) {
         this.generateBox(level, chunkBB, 37, 0, 21, 43, 0, 43, BASE_GRAY, BASE_GRAY, false);
         this.generateWaterBox(level, chunkBB, 37, 1, 22, 43, 14, 43);
         this.generateBox(level, chunkBB, 37, 12, 22, 39, 12, 39, BASE_GRAY, BASE_GRAY, false);
         this.generateBox(level, chunkBB, 37, 12, 21, 39, 12, 21, BASE_LIGHT, BASE_LIGHT, false);

         for (int i = 0; i < 4; i++) {
            this.generateBox(level, chunkBB, 43 - i, i + 9, 21, 43 - i, i + 9, 43 - i, BASE_LIGHT, BASE_LIGHT, false);
         }

         for (int z = 23; z <= 39; z += 3) {
            this.placeBlock(level, DOT_DECO_DATA, 38, 13, z, chunkBB);
         }
      }

      if (this.chunkIntersects(chunkBB, 15, 37, 42, 43)) {
         this.generateBox(level, chunkBB, 21, 0, 37, 36, 0, 43, BASE_GRAY, BASE_GRAY, false);
         this.generateWaterBox(level, chunkBB, 21, 1, 37, 36, 14, 43);
         this.generateBox(level, chunkBB, 21, 12, 37, 36, 12, 39, BASE_GRAY, BASE_GRAY, false);

         for (int i = 0; i < 4; i++) {
            this.generateBox(level, chunkBB, 15 + i, i + 9, 43 - i, 42 - i, i + 9, 43 - i, BASE_LIGHT, BASE_LIGHT, false);
         }

         for (int x = 21; x <= 36; x += 3) {
            this.placeBlock(level, DOT_DECO_DATA, x, 13, 38, chunkBB);
         }
      }
   }
}
