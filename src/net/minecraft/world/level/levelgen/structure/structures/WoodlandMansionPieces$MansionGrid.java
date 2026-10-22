package net.minecraft.world.level.levelgen.structure.structures;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction$Plane;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import org.jspecify.annotations.Nullable;

class WoodlandMansionPieces$MansionGrid {
   private static final int DEFAULT_SIZE = 11;
   private static final int CLEAR = 0;
   private static final int CORRIDOR = 1;
   private static final int ROOM = 2;
   private static final int START_ROOM = 3;
   private static final int TEST_ROOM = 4;
   private static final int BLOCKED = 5;
   private static final int ROOM_1x1 = 65536;
   private static final int ROOM_1x2 = 131072;
   private static final int ROOM_2x2 = 262144;
   private static final int ROOM_ORIGIN_FLAG = 1048576;
   private static final int ROOM_DOOR_FLAG = 2097152;
   private static final int ROOM_STAIRS_FLAG = 4194304;
   private static final int ROOM_CORRIDOR_FLAG = 8388608;
   private static final int ROOM_TYPE_MASK = 983040;
   private static final int ROOM_ID_MASK = 65535;
   private final RandomSource random;
   private final WoodlandMansionPieces$SimpleGrid baseGrid;
   private final WoodlandMansionPieces$SimpleGrid thirdFloorGrid;
   private final WoodlandMansionPieces$SimpleGrid[] floorRooms;
   private final int entranceX;
   private final int entranceY;

   public WoodlandMansionPieces$MansionGrid(final RandomSource random) {
      this.random = random;
      int houseSize = 11;
      this.entranceX = 7;
      this.entranceY = 4;
      this.baseGrid = new WoodlandMansionPieces$SimpleGrid(11, 11, 5);
      this.baseGrid.set(this.entranceX, this.entranceY, this.entranceX + 1, this.entranceY + 1, 3);
      this.baseGrid.set(this.entranceX - 1, this.entranceY, this.entranceX - 1, this.entranceY + 1, 2);
      this.baseGrid.set(this.entranceX + 2, this.entranceY - 2, this.entranceX + 3, this.entranceY + 3, 5);
      this.baseGrid.set(this.entranceX + 1, this.entranceY - 2, this.entranceX + 1, this.entranceY - 1, 1);
      this.baseGrid.set(this.entranceX + 1, this.entranceY + 2, this.entranceX + 1, this.entranceY + 3, 1);
      this.baseGrid.set(this.entranceX - 1, this.entranceY - 1, 1);
      this.baseGrid.set(this.entranceX - 1, this.entranceY + 2, 1);
      this.baseGrid.set(0, 0, 11, 1, 5);
      this.baseGrid.set(0, 9, 11, 11, 5);
      this.recursiveCorridor(this.baseGrid, this.entranceX, this.entranceY - 2, Direction.WEST, 6);
      this.recursiveCorridor(this.baseGrid, this.entranceX, this.entranceY + 3, Direction.WEST, 6);
      this.recursiveCorridor(this.baseGrid, this.entranceX - 2, this.entranceY - 1, Direction.WEST, 3);
      this.recursiveCorridor(this.baseGrid, this.entranceX - 2, this.entranceY + 2, Direction.WEST, 3);

      while (this.cleanEdges(this.baseGrid)) {
      }

      this.floorRooms = new WoodlandMansionPieces$SimpleGrid[3];
      this.floorRooms[0] = new WoodlandMansionPieces$SimpleGrid(11, 11, 5);
      this.floorRooms[1] = new WoodlandMansionPieces$SimpleGrid(11, 11, 5);
      this.floorRooms[2] = new WoodlandMansionPieces$SimpleGrid(11, 11, 5);
      this.identifyRooms(this.baseGrid, this.floorRooms[0]);
      this.identifyRooms(this.baseGrid, this.floorRooms[1]);
      this.floorRooms[0].set(this.entranceX + 1, this.entranceY, this.entranceX + 1, this.entranceY + 1, 8388608);
      this.floorRooms[1].set(this.entranceX + 1, this.entranceY, this.entranceX + 1, this.entranceY + 1, 8388608);
      this.thirdFloorGrid = new WoodlandMansionPieces$SimpleGrid(this.baseGrid.width, this.baseGrid.height, 5);
      this.setupThirdFloor();
      this.identifyRooms(this.thirdFloorGrid, this.floorRooms[2]);
   }

   public static boolean isHouse(final WoodlandMansionPieces$SimpleGrid grid, final int x, final int y) {
      int value = grid.get(x, y);
      return value == 1 || value == 2 || value == 3 || value == 4;
   }

   public boolean isRoomId(final WoodlandMansionPieces$SimpleGrid grid, final int x, final int y, final int floor, final int roomId) {
      return (this.floorRooms[floor].get(x, y) & 65535) == roomId;
   }

   @Nullable
   public Direction get1x2RoomDirection(final WoodlandMansionPieces$SimpleGrid grid, final int x, final int y, final int floorNum, final int roomId) {
      for (Direction direction : Direction$Plane.HORIZONTAL) {
         if (this.isRoomId(grid, x + direction.getStepX(), y + direction.getStepZ(), floorNum, roomId)) {
            return direction;
         }
      }

      return null;
   }

   private void recursiveCorridor(final WoodlandMansionPieces$SimpleGrid grid, final int x, final int y, final Direction heading, final int depth) {
      if (depth > 0) {
         grid.set(x, y, 1);
         grid.setif(x + heading.getStepX(), y + heading.getStepZ(), 0, 1);

         for (int attempts = 0; attempts < 8; attempts++) {
            Direction nextDir = Direction.from2DDataValue(this.random.nextInt(4));
            if (nextDir != heading.getOpposite() && (nextDir != Direction.EAST || !this.random.nextBoolean())) {
               int nx = x + heading.getStepX();
               int ny = y + heading.getStepZ();
               if (grid.get(nx + nextDir.getStepX(), ny + nextDir.getStepZ()) == 0 && grid.get(nx + nextDir.getStepX() * 2, ny + nextDir.getStepZ() * 2) == 0) {
                  this.recursiveCorridor(grid, x + heading.getStepX() + nextDir.getStepX(), y + heading.getStepZ() + nextDir.getStepZ(), nextDir, depth - 1);
                  break;
               }
            }
         }

         Direction cw = heading.getClockWise();
         Direction ccw = heading.getCounterClockWise();
         grid.setif(x + cw.getStepX(), y + cw.getStepZ(), 0, 2);
         grid.setif(x + ccw.getStepX(), y + ccw.getStepZ(), 0, 2);
         grid.setif(x + heading.getStepX() + cw.getStepX(), y + heading.getStepZ() + cw.getStepZ(), 0, 2);
         grid.setif(x + heading.getStepX() + ccw.getStepX(), y + heading.getStepZ() + ccw.getStepZ(), 0, 2);
         grid.setif(x + heading.getStepX() * 2, y + heading.getStepZ() * 2, 0, 2);
         grid.setif(x + cw.getStepX() * 2, y + cw.getStepZ() * 2, 0, 2);
         grid.setif(x + ccw.getStepX() * 2, y + ccw.getStepZ() * 2, 0, 2);
      }
   }

   private boolean cleanEdges(final WoodlandMansionPieces$SimpleGrid grid) {
      boolean touched = false;

      for (int y = 0; y < grid.height; y++) {
         for (int x = 0; x < grid.width; x++) {
            if (grid.get(x, y) == 0) {
               int directNeighbors = 0;
               directNeighbors += isHouse(grid, x + 1, y) ? 1 : 0;
               directNeighbors += isHouse(grid, x - 1, y) ? 1 : 0;
               directNeighbors += isHouse(grid, x, y + 1) ? 1 : 0;
               directNeighbors += isHouse(grid, x, y - 1) ? 1 : 0;
               if (directNeighbors >= 3) {
                  grid.set(x, y, 2);
                  touched = true;
               } else if (directNeighbors == 2) {
                  int diagonalNeighbors = 0;
                  diagonalNeighbors += isHouse(grid, x + 1, y + 1) ? 1 : 0;
                  diagonalNeighbors += isHouse(grid, x - 1, y + 1) ? 1 : 0;
                  diagonalNeighbors += isHouse(grid, x + 1, y - 1) ? 1 : 0;
                  diagonalNeighbors += isHouse(grid, x - 1, y - 1) ? 1 : 0;
                  if (diagonalNeighbors <= 1) {
                     grid.set(x, y, 2);
                     touched = true;
                  }
               }
            }
         }
      }

      return touched;
   }

   private void setupThirdFloor() {
      List<WoodlandMansionPieces$MansionGrid$GridPos> potentialRooms = new ArrayList<>();
      WoodlandMansionPieces$SimpleGrid floor = this.floorRooms[1];

      for (int y = 0; y < this.thirdFloorGrid.height; y++) {
         for (int x = 0; x < this.thirdFloorGrid.width; x++) {
            int roomData = floor.get(x, y);
            int roomType = roomData & 983040;
            if (roomType == 131072 && (roomData & 2097152) == 2097152) {
               potentialRooms.add(new WoodlandMansionPieces$MansionGrid$GridPos(x, y));
            }
         }
      }

      if (potentialRooms.isEmpty()) {
         this.thirdFloorGrid.set(0, 0, this.thirdFloorGrid.width, this.thirdFloorGrid.height, 5);
      } else {
         WoodlandMansionPieces$MansionGrid$GridPos roomPos = potentialRooms.get(this.random.nextInt(potentialRooms.size()));
         int roomData = floor.get(roomPos.x(), roomPos.y());
         floor.set(roomPos.x(), roomPos.y(), roomData | 4194304);
         Direction roomDir = this.get1x2RoomDirection(this.baseGrid, roomPos.x(), roomPos.y(), 1, roomData & 65535);
         int roomEndX = roomPos.x() + roomDir.getStepX();
         int roomEndY = roomPos.y() + roomDir.getStepZ();

         for (int y = 0; y < this.thirdFloorGrid.height; y++) {
            for (int xx = 0; xx < this.thirdFloorGrid.width; xx++) {
               if (!isHouse(this.baseGrid, xx, y)) {
                  this.thirdFloorGrid.set(xx, y, 5);
               } else if (xx == roomPos.x() && y == roomPos.y()) {
                  this.thirdFloorGrid.set(xx, y, 3);
               } else if (xx == roomEndX && y == roomEndY) {
                  this.thirdFloorGrid.set(xx, y, 3);
                  this.floorRooms[2].set(xx, y, 8388608);
               }
            }
         }

         List<Direction> potentialCorridors = new ArrayList<>();

         for (Direction direction : Direction$Plane.HORIZONTAL) {
            if (this.thirdFloorGrid.get(roomEndX + direction.getStepX(), roomEndY + direction.getStepZ()) == 0) {
               potentialCorridors.add(direction);
            }
         }

         if (potentialCorridors.isEmpty()) {
            this.thirdFloorGrid.set(0, 0, this.thirdFloorGrid.width, this.thirdFloorGrid.height, 5);
            floor.set(roomPos.x(), roomPos.y(), roomData);
         } else {
            Direction corridorDir = potentialCorridors.get(this.random.nextInt(potentialCorridors.size()));
            this.recursiveCorridor(this.thirdFloorGrid, roomEndX + corridorDir.getStepX(), roomEndY + corridorDir.getStepZ(), corridorDir, 4);

            while (this.cleanEdges(this.thirdFloorGrid)) {
            }
         }
      }
   }

   private void identifyRooms(final WoodlandMansionPieces$SimpleGrid fromGrid, final WoodlandMansionPieces$SimpleGrid roomGrid) {
      List<WoodlandMansionPieces$MansionGrid$GridPos> roomPos = new ArrayList<>();

      for (int y = 0; y < fromGrid.height; y++) {
         for (int x = 0; x < fromGrid.width; x++) {
            if (fromGrid.get(x, y) == 2) {
               roomPos.add(new WoodlandMansionPieces$MansionGrid$GridPos(x, y));
            }
         }
      }

      Util.shuffle(roomPos, this.random);
      int roomId = 10;

      for (WoodlandMansionPieces$MansionGrid$GridPos pos : roomPos) {
         int xx = pos.x();
         int y = pos.y();
         if (roomGrid.get(xx, y) == 0) {
            int x0 = xx;
            int x1 = xx;
            int y0 = y;
            int y1 = y;
            int type = 65536;
            if (roomGrid.get(xx + 1, y) == 0
               && roomGrid.get(xx, y + 1) == 0
               && roomGrid.get(xx + 1, y + 1) == 0
               && fromGrid.get(xx + 1, y) == 2
               && fromGrid.get(xx, y + 1) == 2
               && fromGrid.get(xx + 1, y + 1) == 2) {
               x1 = xx + 1;
               y1 = y + 1;
               type = 262144;
            } else if (roomGrid.get(xx - 1, y) == 0
               && roomGrid.get(xx, y + 1) == 0
               && roomGrid.get(xx - 1, y + 1) == 0
               && fromGrid.get(xx - 1, y) == 2
               && fromGrid.get(xx, y + 1) == 2
               && fromGrid.get(xx - 1, y + 1) == 2) {
               x0 = xx - 1;
               y1 = y + 1;
               type = 262144;
            } else if (roomGrid.get(xx - 1, y) == 0
               && roomGrid.get(xx, y - 1) == 0
               && roomGrid.get(xx - 1, y - 1) == 0
               && fromGrid.get(xx - 1, y) == 2
               && fromGrid.get(xx, y - 1) == 2
               && fromGrid.get(xx - 1, y - 1) == 2) {
               x0 = xx - 1;
               y0 = y - 1;
               type = 262144;
            } else if (roomGrid.get(xx + 1, y) == 0 && fromGrid.get(xx + 1, y) == 2) {
               x1 = xx + 1;
               type = 131072;
            } else if (roomGrid.get(xx, y + 1) == 0 && fromGrid.get(xx, y + 1) == 2) {
               y1 = y + 1;
               type = 131072;
            } else if (roomGrid.get(xx - 1, y) == 0 && fromGrid.get(xx - 1, y) == 2) {
               x0 = xx - 1;
               type = 131072;
            } else if (roomGrid.get(xx, y - 1) == 0 && fromGrid.get(xx, y - 1) == 2) {
               y0 = y - 1;
               type = 131072;
            }

            int doorX = this.random.nextBoolean() ? x0 : x1;
            int doorY = this.random.nextBoolean() ? y0 : y1;
            int doorFlag = 2097152;
            if (!fromGrid.edgesTo(doorX, doorY, 1)) {
               doorX = doorX == x0 ? x1 : x0;
               doorY = doorY == y0 ? y1 : y0;
               if (!fromGrid.edgesTo(doorX, doorY, 1)) {
                  doorY = doorY == y0 ? y1 : y0;
                  if (!fromGrid.edgesTo(doorX, doorY, 1)) {
                     doorX = doorX == x0 ? x1 : x0;
                     doorY = doorY == y0 ? y1 : y0;
                     if (!fromGrid.edgesTo(doorX, doorY, 1)) {
                        doorFlag = 0;
                        doorX = x0;
                        doorY = y0;
                     }
                  }
               }
            }

            for (int ry = y0; ry <= y1; ry++) {
               for (int rx = x0; rx <= x1; rx++) {
                  if (rx == doorX && ry == doorY) {
                     roomGrid.set(rx, ry, 1048576 | doorFlag | type | roomId);
                  } else {
                     roomGrid.set(rx, ry, type | roomId);
                  }
               }
            }

            roomId++;
         }
      }
   }
}
