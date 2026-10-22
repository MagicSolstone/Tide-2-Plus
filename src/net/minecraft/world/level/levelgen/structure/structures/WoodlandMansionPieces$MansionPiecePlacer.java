package net.minecraft.world.level.levelgen.structure.structures;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction$Plane;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.jspecify.annotations.Nullable;

class WoodlandMansionPieces$MansionPiecePlacer {
   private final StructureTemplateManager structureTemplateManager;
   private final RandomSource random;
   private int startX;
   private int startY;

   public WoodlandMansionPieces$MansionPiecePlacer(final StructureTemplateManager structureTemplateManager, final RandomSource random) {
      this.structureTemplateManager = structureTemplateManager;
      this.random = random;
   }

   public void createMansion(
      final BlockPos origin,
      final Rotation rotation,
      final List<WoodlandMansionPieces$WoodlandMansionPiece> pieces,
      final WoodlandMansionPieces$MansionGrid mansion
   ) {
      WoodlandMansionPieces$PlacementData data = new WoodlandMansionPieces$PlacementData();
      data.position = origin;
      data.rotation = rotation;
      data.wallType = "wall_flat";
      WoodlandMansionPieces$PlacementData secondData = new WoodlandMansionPieces$PlacementData();
      this.entrance(pieces, data);
      secondData.position = data.position.above(8);
      secondData.rotation = data.rotation;
      secondData.wallType = "wall_window";
      if (!pieces.isEmpty()) {
      }

      WoodlandMansionPieces$SimpleGrid baseGrid = mansion.baseGrid;
      WoodlandMansionPieces$SimpleGrid thirdGrid = mansion.thirdFloorGrid;
      this.startX = mansion.entranceX + 1;
      this.startY = mansion.entranceY + 1;
      int endX = mansion.entranceX + 1;
      int endY = mansion.entranceY;
      this.traverseOuterWalls(pieces, data, baseGrid, Direction.SOUTH, this.startX, this.startY, endX, endY);
      this.traverseOuterWalls(pieces, secondData, baseGrid, Direction.SOUTH, this.startX, this.startY, endX, endY);
      WoodlandMansionPieces$PlacementData thirdData = new WoodlandMansionPieces$PlacementData();
      thirdData.position = data.position.above(19);
      thirdData.rotation = data.rotation;
      thirdData.wallType = "wall_window";
      boolean done = false;

      for (int y = 0; y < thirdGrid.height && !done; y++) {
         for (int x = thirdGrid.width - 1; x >= 0 && !done; x--) {
            if (WoodlandMansionPieces$MansionGrid.isHouse(thirdGrid, x, y)) {
               thirdData.position = thirdData.position.relative(rotation.rotate(Direction.SOUTH), 8 + (y - this.startY) * 8);
               thirdData.position = thirdData.position.relative(rotation.rotate(Direction.EAST), (x - this.startX) * 8);
               this.traverseWallPiece(pieces, thirdData);
               this.traverseOuterWalls(pieces, thirdData, thirdGrid, Direction.SOUTH, x, y, x, y);
               done = true;
            }
         }
      }

      this.createRoof(pieces, origin.above(16), rotation, baseGrid, thirdGrid);
      this.createRoof(pieces, origin.above(27), rotation, thirdGrid, null);
      if (!pieces.isEmpty()) {
      }

      WoodlandMansionPieces$FloorRoomCollection[] roomCollections = new WoodlandMansionPieces$FloorRoomCollection[]{
         new WoodlandMansionPieces$FirstFloorRoomCollection(),
         new WoodlandMansionPieces$SecondFloorRoomCollection(),
         new WoodlandMansionPieces$ThirdFloorRoomCollection()
      };

      for (int floorNum = 0; floorNum < 3; floorNum++) {
         BlockPos floorOrigin = origin.above(8 * floorNum + (floorNum == 2 ? 3 : 0));
         WoodlandMansionPieces$SimpleGrid rooms = mansion.floorRooms[floorNum];
         WoodlandMansionPieces$SimpleGrid grid = floorNum == 2 ? thirdGrid : baseGrid;
         String southPiece = floorNum == 0 ? "carpet_south_1" : "carpet_south_2";
         String westPiece = floorNum == 0 ? "carpet_west_1" : "carpet_west_2";

         for (int y = 0; y < grid.height; y++) {
            for (int xx = 0; xx < grid.width; xx++) {
               if (grid.get(xx, y) == 1) {
                  BlockPos pos = floorOrigin.relative(rotation.rotate(Direction.SOUTH), 8 + (y - this.startY) * 8);
                  pos = pos.relative(rotation.rotate(Direction.EAST), (xx - this.startX) * 8);
                  pieces.add(new WoodlandMansionPieces$WoodlandMansionPiece(this.structureTemplateManager, "corridor_floor", pos, rotation));
                  if (grid.get(xx, y - 1) == 1 || (rooms.get(xx, y - 1) & 8388608) == 8388608) {
                     pieces.add(
                        new WoodlandMansionPieces$WoodlandMansionPiece(
                           this.structureTemplateManager, "carpet_north", pos.relative(rotation.rotate(Direction.EAST), 1).above(), rotation
                        )
                     );
                  }

                  if (grid.get(xx + 1, y) == 1 || (rooms.get(xx + 1, y) & 8388608) == 8388608) {
                     pieces.add(
                        new WoodlandMansionPieces$WoodlandMansionPiece(
                           this.structureTemplateManager,
                           "carpet_east",
                           pos.relative(rotation.rotate(Direction.SOUTH), 1).relative(rotation.rotate(Direction.EAST), 5).above(),
                           rotation
                        )
                     );
                  }

                  if (grid.get(xx, y + 1) == 1 || (rooms.get(xx, y + 1) & 8388608) == 8388608) {
                     pieces.add(
                        new WoodlandMansionPieces$WoodlandMansionPiece(
                           this.structureTemplateManager,
                           southPiece,
                           pos.relative(rotation.rotate(Direction.SOUTH), 5).relative(rotation.rotate(Direction.WEST), 1),
                           rotation
                        )
                     );
                  }

                  if (grid.get(xx - 1, y) == 1 || (rooms.get(xx - 1, y) & 8388608) == 8388608) {
                     pieces.add(
                        new WoodlandMansionPieces$WoodlandMansionPiece(
                           this.structureTemplateManager,
                           westPiece,
                           pos.relative(rotation.rotate(Direction.WEST), 1).relative(rotation.rotate(Direction.NORTH), 1),
                           rotation
                        )
                     );
                  }
               }
            }
         }

         String wallPiece = floorNum == 0 ? "indoors_wall_1" : "indoors_wall_2";
         String doorPiece = floorNum == 0 ? "indoors_door_1" : "indoors_door_2";
         List<Direction> doorDirs = new ArrayList<>();

         for (int y = 0; y < grid.height; y++) {
            for (int xxx = 0; xxx < grid.width; xxx++) {
               boolean thirdFloorStartRoom = floorNum == 2 && grid.get(xxx, y) == 3;
               if (grid.get(xxx, y) == 2 || thirdFloorStartRoom) {
                  int roomData = rooms.get(xxx, y);
                  int roomType = roomData & 983040;
                  int roomId = roomData & 65535;
                  thirdFloorStartRoom = thirdFloorStartRoom && (roomData & 8388608) == 8388608;
                  doorDirs.clear();
                  if ((roomData & 2097152) == 2097152) {
                     for (Direction direction : Direction$Plane.HORIZONTAL) {
                        if (grid.get(xxx + direction.getStepX(), y + direction.getStepZ()) == 1) {
                           doorDirs.add(direction);
                        }
                     }
                  }

                  Direction doorDir = null;
                  if (!doorDirs.isEmpty()) {
                     doorDir = doorDirs.get(this.random.nextInt(doorDirs.size()));
                  } else if ((roomData & 1048576) == 1048576) {
                     doorDir = Direction.UP;
                  }

                  BlockPos roomPos = floorOrigin.relative(rotation.rotate(Direction.SOUTH), 8 + (y - this.startY) * 8);
                  roomPos = roomPos.relative(rotation.rotate(Direction.EAST), -1 + (xxx - this.startX) * 8);
                  if (WoodlandMansionPieces$MansionGrid.isHouse(grid, xxx - 1, y) && !mansion.isRoomId(grid, xxx - 1, y, floorNum, roomId)) {
                     pieces.add(
                        new WoodlandMansionPieces$WoodlandMansionPiece(
                           this.structureTemplateManager, doorDir == Direction.WEST ? doorPiece : wallPiece, roomPos, rotation
                        )
                     );
                  }

                  if (grid.get(xxx + 1, y) == 1 && !thirdFloorStartRoom) {
                     BlockPos posx = roomPos.relative(rotation.rotate(Direction.EAST), 8);
                     pieces.add(
                        new WoodlandMansionPieces$WoodlandMansionPiece(
                           this.structureTemplateManager, doorDir == Direction.EAST ? doorPiece : wallPiece, posx, rotation
                        )
                     );
                  }

                  if (WoodlandMansionPieces$MansionGrid.isHouse(grid, xxx, y + 1) && !mansion.isRoomId(grid, xxx, y + 1, floorNum, roomId)) {
                     BlockPos posx = roomPos.relative(rotation.rotate(Direction.SOUTH), 7);
                     posx = posx.relative(rotation.rotate(Direction.EAST), 7);
                     pieces.add(
                        new WoodlandMansionPieces$WoodlandMansionPiece(
                           this.structureTemplateManager, doorDir == Direction.SOUTH ? doorPiece : wallPiece, posx, rotation.getRotated(Rotation.CLOCKWISE_90)
                        )
                     );
                  }

                  if (grid.get(xxx, y - 1) == 1 && !thirdFloorStartRoom) {
                     BlockPos posx = roomPos.relative(rotation.rotate(Direction.NORTH), 1);
                     posx = posx.relative(rotation.rotate(Direction.EAST), 7);
                     pieces.add(
                        new WoodlandMansionPieces$WoodlandMansionPiece(
                           this.structureTemplateManager, doorDir == Direction.NORTH ? doorPiece : wallPiece, posx, rotation.getRotated(Rotation.CLOCKWISE_90)
                        )
                     );
                  }

                  if (roomType == 65536) {
                     this.addRoom1x1(pieces, roomPos, rotation, doorDir, roomCollections[floorNum]);
                  } else if (roomType == 131072 && doorDir != null) {
                     Direction roomDir = mansion.get1x2RoomDirection(grid, xxx, y, floorNum, roomId);
                     boolean isStairsRoom = (roomData & 4194304) == 4194304;
                     this.addRoom1x2(pieces, roomPos, rotation, roomDir, doorDir, roomCollections[floorNum], isStairsRoom);
                  } else if (roomType == 262144 && doorDir != null && doorDir != Direction.UP) {
                     Direction roomDir = doorDir.getClockWise();
                     if (!mansion.isRoomId(grid, xxx + roomDir.getStepX(), y + roomDir.getStepZ(), floorNum, roomId)) {
                        roomDir = roomDir.getOpposite();
                     }

                     this.addRoom2x2(pieces, roomPos, rotation, roomDir, doorDir, roomCollections[floorNum]);
                  } else if (roomType == 262144 && doorDir == Direction.UP) {
                     this.addRoom2x2Secret(pieces, roomPos, rotation, roomCollections[floorNum]);
                  }
               }
            }
         }
      }
   }

   private void traverseOuterWalls(
      final List<WoodlandMansionPieces$WoodlandMansionPiece> pieces,
      final WoodlandMansionPieces$PlacementData data,
      final WoodlandMansionPieces$SimpleGrid grid,
      Direction gridDirection,
      final int startX,
      final int startY,
      final int endX,
      final int endY
   ) {
      int gridX = startX;
      int gridY = startY;
      Direction startDirection = gridDirection;

      do {
         if (!WoodlandMansionPieces$MansionGrid.isHouse(grid, gridX + gridDirection.getStepX(), gridY + gridDirection.getStepZ())) {
            this.traverseTurn(pieces, data);
            gridDirection = gridDirection.getClockWise();
            if (gridX != endX || gridY != endY || startDirection != gridDirection) {
               this.traverseWallPiece(pieces, data);
            }
         } else if (WoodlandMansionPieces$MansionGrid.isHouse(grid, gridX + gridDirection.getStepX(), gridY + gridDirection.getStepZ())
            && WoodlandMansionPieces$MansionGrid.isHouse(
               grid,
               gridX + gridDirection.getStepX() + gridDirection.getCounterClockWise().getStepX(),
               gridY + gridDirection.getStepZ() + gridDirection.getCounterClockWise().getStepZ()
            )) {
            this.traverseInnerTurn(pieces, data);
            gridX += gridDirection.getStepX();
            gridY += gridDirection.getStepZ();
            gridDirection = gridDirection.getCounterClockWise();
         } else {
            gridX += gridDirection.getStepX();
            gridY += gridDirection.getStepZ();
            if (gridX != endX || gridY != endY || startDirection != gridDirection) {
               this.traverseWallPiece(pieces, data);
            }
         }
      } while (gridX != endX || gridY != endY || startDirection != gridDirection);
   }

   private void createRoof(
      final List<WoodlandMansionPieces$WoodlandMansionPiece> pieces,
      final BlockPos roofOrigin,
      final Rotation rotation,
      final WoodlandMansionPieces$SimpleGrid grid,
      @Nullable final WoodlandMansionPieces$SimpleGrid aboveGrid
   ) {
      for (int y = 0; y < grid.height; y++) {
         for (int x = 0; x < grid.width; x++) {
            BlockPos position = roofOrigin.relative(rotation.rotate(Direction.SOUTH), 8 + (y - this.startY) * 8);
            position = position.relative(rotation.rotate(Direction.EAST), (x - this.startX) * 8);
            boolean isAbove = aboveGrid != null && WoodlandMansionPieces$MansionGrid.isHouse(aboveGrid, x, y);
            if (WoodlandMansionPieces$MansionGrid.isHouse(grid, x, y) && !isAbove) {
               pieces.add(new WoodlandMansionPieces$WoodlandMansionPiece(this.structureTemplateManager, "roof", position.above(3), rotation));
               if (!WoodlandMansionPieces$MansionGrid.isHouse(grid, x + 1, y)) {
                  BlockPos p2 = position.relative(rotation.rotate(Direction.EAST), 6);
                  pieces.add(new WoodlandMansionPieces$WoodlandMansionPiece(this.structureTemplateManager, "roof_front", p2, rotation));
               }

               if (!WoodlandMansionPieces$MansionGrid.isHouse(grid, x - 1, y)) {
                  BlockPos p2 = position.relative(rotation.rotate(Direction.EAST), 0);
                  p2 = p2.relative(rotation.rotate(Direction.SOUTH), 7);
                  pieces.add(
                     new WoodlandMansionPieces$WoodlandMansionPiece(
                        this.structureTemplateManager, "roof_front", p2, rotation.getRotated(Rotation.CLOCKWISE_180)
                     )
                  );
               }

               if (!WoodlandMansionPieces$MansionGrid.isHouse(grid, x, y - 1)) {
                  BlockPos p2 = position.relative(rotation.rotate(Direction.WEST), 1);
                  pieces.add(
                     new WoodlandMansionPieces$WoodlandMansionPiece(
                        this.structureTemplateManager, "roof_front", p2, rotation.getRotated(Rotation.COUNTERCLOCKWISE_90)
                     )
                  );
               }

               if (!WoodlandMansionPieces$MansionGrid.isHouse(grid, x, y + 1)) {
                  BlockPos p2 = position.relative(rotation.rotate(Direction.EAST), 6);
                  p2 = p2.relative(rotation.rotate(Direction.SOUTH), 6);
                  pieces.add(
                     new WoodlandMansionPieces$WoodlandMansionPiece(this.structureTemplateManager, "roof_front", p2, rotation.getRotated(Rotation.CLOCKWISE_90))
                  );
               }
            }
         }
      }

      if (aboveGrid != null) {
         for (int y = 0; y < grid.height; y++) {
            for (int xx = 0; xx < grid.width; xx++) {
               BlockPos var17 = roofOrigin.relative(rotation.rotate(Direction.SOUTH), 8 + (y - this.startY) * 8);
               var17 = var17.relative(rotation.rotate(Direction.EAST), (xx - this.startX) * 8);
               boolean isAbove = WoodlandMansionPieces$MansionGrid.isHouse(aboveGrid, xx, y);
               if (WoodlandMansionPieces$MansionGrid.isHouse(grid, xx, y) && isAbove) {
                  if (!WoodlandMansionPieces$MansionGrid.isHouse(grid, xx + 1, y)) {
                     BlockPos p2 = var17.relative(rotation.rotate(Direction.EAST), 7);
                     pieces.add(new WoodlandMansionPieces$WoodlandMansionPiece(this.structureTemplateManager, "small_wall", p2, rotation));
                  }

                  if (!WoodlandMansionPieces$MansionGrid.isHouse(grid, xx - 1, y)) {
                     BlockPos p2 = var17.relative(rotation.rotate(Direction.WEST), 1);
                     p2 = p2.relative(rotation.rotate(Direction.SOUTH), 6);
                     pieces.add(
                        new WoodlandMansionPieces$WoodlandMansionPiece(
                           this.structureTemplateManager, "small_wall", p2, rotation.getRotated(Rotation.CLOCKWISE_180)
                        )
                     );
                  }

                  if (!WoodlandMansionPieces$MansionGrid.isHouse(grid, xx, y - 1)) {
                     BlockPos p2 = var17.relative(rotation.rotate(Direction.WEST), 0);
                     p2 = p2.relative(rotation.rotate(Direction.NORTH), 1);
                     pieces.add(
                        new WoodlandMansionPieces$WoodlandMansionPiece(
                           this.structureTemplateManager, "small_wall", p2, rotation.getRotated(Rotation.COUNTERCLOCKWISE_90)
                        )
                     );
                  }

                  if (!WoodlandMansionPieces$MansionGrid.isHouse(grid, xx, y + 1)) {
                     BlockPos p2 = var17.relative(rotation.rotate(Direction.EAST), 6);
                     p2 = p2.relative(rotation.rotate(Direction.SOUTH), 7);
                     pieces.add(
                        new WoodlandMansionPieces$WoodlandMansionPiece(
                           this.structureTemplateManager, "small_wall", p2, rotation.getRotated(Rotation.CLOCKWISE_90)
                        )
                     );
                  }

                  if (!WoodlandMansionPieces$MansionGrid.isHouse(grid, xx + 1, y)) {
                     if (!WoodlandMansionPieces$MansionGrid.isHouse(grid, xx, y - 1)) {
                        BlockPos p2 = var17.relative(rotation.rotate(Direction.EAST), 7);
                        p2 = p2.relative(rotation.rotate(Direction.NORTH), 2);
                        pieces.add(new WoodlandMansionPieces$WoodlandMansionPiece(this.structureTemplateManager, "small_wall_corner", p2, rotation));
                     }

                     if (!WoodlandMansionPieces$MansionGrid.isHouse(grid, xx, y + 1)) {
                        BlockPos p2 = var17.relative(rotation.rotate(Direction.EAST), 8);
                        p2 = p2.relative(rotation.rotate(Direction.SOUTH), 7);
                        pieces.add(
                           new WoodlandMansionPieces$WoodlandMansionPiece(
                              this.structureTemplateManager, "small_wall_corner", p2, rotation.getRotated(Rotation.CLOCKWISE_90)
                           )
                        );
                     }
                  }

                  if (!WoodlandMansionPieces$MansionGrid.isHouse(grid, xx - 1, y)) {
                     if (!WoodlandMansionPieces$MansionGrid.isHouse(grid, xx, y - 1)) {
                        BlockPos p2 = var17.relative(rotation.rotate(Direction.WEST), 2);
                        p2 = p2.relative(rotation.rotate(Direction.NORTH), 1);
                        pieces.add(
                           new WoodlandMansionPieces$WoodlandMansionPiece(
                              this.structureTemplateManager, "small_wall_corner", p2, rotation.getRotated(Rotation.COUNTERCLOCKWISE_90)
                           )
                        );
                     }

                     if (!WoodlandMansionPieces$MansionGrid.isHouse(grid, xx, y + 1)) {
                        BlockPos p2 = var17.relative(rotation.rotate(Direction.WEST), 1);
                        p2 = p2.relative(rotation.rotate(Direction.SOUTH), 8);
                        pieces.add(
                           new WoodlandMansionPieces$WoodlandMansionPiece(
                              this.structureTemplateManager, "small_wall_corner", p2, rotation.getRotated(Rotation.CLOCKWISE_180)
                           )
                        );
                     }
                  }
               }
            }
         }
      }

      for (int y = 0; y < grid.height; y++) {
         for (int xxx = 0; xxx < grid.width; xxx++) {
            BlockPos var19 = roofOrigin.relative(rotation.rotate(Direction.SOUTH), 8 + (y - this.startY) * 8);
            var19 = var19.relative(rotation.rotate(Direction.EAST), (xxx - this.startX) * 8);
            boolean isAbove = aboveGrid != null && WoodlandMansionPieces$MansionGrid.isHouse(aboveGrid, xxx, y);
            if (WoodlandMansionPieces$MansionGrid.isHouse(grid, xxx, y) && !isAbove) {
               if (!WoodlandMansionPieces$MansionGrid.isHouse(grid, xxx + 1, y)) {
                  BlockPos p2 = var19.relative(rotation.rotate(Direction.EAST), 6);
                  if (!WoodlandMansionPieces$MansionGrid.isHouse(grid, xxx, y + 1)) {
                     BlockPos p3 = p2.relative(rotation.rotate(Direction.SOUTH), 6);
                     pieces.add(new WoodlandMansionPieces$WoodlandMansionPiece(this.structureTemplateManager, "roof_corner", p3, rotation));
                  } else if (WoodlandMansionPieces$MansionGrid.isHouse(grid, xxx + 1, y + 1)) {
                     BlockPos p3 = p2.relative(rotation.rotate(Direction.SOUTH), 5);
                     pieces.add(new WoodlandMansionPieces$WoodlandMansionPiece(this.structureTemplateManager, "roof_inner_corner", p3, rotation));
                  }

                  if (!WoodlandMansionPieces$MansionGrid.isHouse(grid, xxx, y - 1)) {
                     pieces.add(
                        new WoodlandMansionPieces$WoodlandMansionPiece(
                           this.structureTemplateManager, "roof_corner", p2, rotation.getRotated(Rotation.COUNTERCLOCKWISE_90)
                        )
                     );
                  } else if (WoodlandMansionPieces$MansionGrid.isHouse(grid, xxx + 1, y - 1)) {
                     BlockPos p3 = var19.relative(rotation.rotate(Direction.EAST), 9);
                     p3 = p3.relative(rotation.rotate(Direction.NORTH), 2);
                     pieces.add(
                        new WoodlandMansionPieces$WoodlandMansionPiece(
                           this.structureTemplateManager, "roof_inner_corner", p3, rotation.getRotated(Rotation.CLOCKWISE_90)
                        )
                     );
                  }
               }

               if (!WoodlandMansionPieces$MansionGrid.isHouse(grid, xxx - 1, y)) {
                  BlockPos p2x = var19.relative(rotation.rotate(Direction.EAST), 0);
                  p2x = p2x.relative(rotation.rotate(Direction.SOUTH), 0);
                  if (!WoodlandMansionPieces$MansionGrid.isHouse(grid, xxx, y + 1)) {
                     BlockPos p3 = p2x.relative(rotation.rotate(Direction.SOUTH), 6);
                     pieces.add(
                        new WoodlandMansionPieces$WoodlandMansionPiece(
                           this.structureTemplateManager, "roof_corner", p3, rotation.getRotated(Rotation.CLOCKWISE_90)
                        )
                     );
                  } else if (WoodlandMansionPieces$MansionGrid.isHouse(grid, xxx - 1, y + 1)) {
                     BlockPos p3 = p2x.relative(rotation.rotate(Direction.SOUTH), 8);
                     p3 = p3.relative(rotation.rotate(Direction.WEST), 3);
                     pieces.add(
                        new WoodlandMansionPieces$WoodlandMansionPiece(
                           this.structureTemplateManager, "roof_inner_corner", p3, rotation.getRotated(Rotation.COUNTERCLOCKWISE_90)
                        )
                     );
                  }

                  if (!WoodlandMansionPieces$MansionGrid.isHouse(grid, xxx, y - 1)) {
                     pieces.add(
                        new WoodlandMansionPieces$WoodlandMansionPiece(
                           this.structureTemplateManager, "roof_corner", p2x, rotation.getRotated(Rotation.CLOCKWISE_180)
                        )
                     );
                  } else if (WoodlandMansionPieces$MansionGrid.isHouse(grid, xxx - 1, y - 1)) {
                     BlockPos p3 = p2x.relative(rotation.rotate(Direction.SOUTH), 1);
                     pieces.add(
                        new WoodlandMansionPieces$WoodlandMansionPiece(
                           this.structureTemplateManager, "roof_inner_corner", p3, rotation.getRotated(Rotation.CLOCKWISE_180)
                        )
                     );
                  }
               }
            }
         }
      }
   }

   private void entrance(final List<WoodlandMansionPieces$WoodlandMansionPiece> pieces, final WoodlandMansionPieces$PlacementData data) {
      Direction west = data.rotation.rotate(Direction.WEST);
      pieces.add(new WoodlandMansionPieces$WoodlandMansionPiece(this.structureTemplateManager, "entrance", data.position.relative(west, 9), data.rotation));
      data.position = data.position.relative(data.rotation.rotate(Direction.SOUTH), 16);
   }

   private void traverseWallPiece(final List<WoodlandMansionPieces$WoodlandMansionPiece> pieces, final WoodlandMansionPieces$PlacementData data) {
      pieces.add(
         new WoodlandMansionPieces$WoodlandMansionPiece(
            this.structureTemplateManager, data.wallType, data.position.relative(data.rotation.rotate(Direction.EAST), 7), data.rotation
         )
      );
      data.position = data.position.relative(data.rotation.rotate(Direction.SOUTH), 8);
   }

   private void traverseTurn(final List<WoodlandMansionPieces$WoodlandMansionPiece> pieces, final WoodlandMansionPieces$PlacementData data) {
      data.position = data.position.relative(data.rotation.rotate(Direction.SOUTH), -1);
      pieces.add(new WoodlandMansionPieces$WoodlandMansionPiece(this.structureTemplateManager, "wall_corner", data.position, data.rotation));
      data.position = data.position.relative(data.rotation.rotate(Direction.SOUTH), -7);
      data.position = data.position.relative(data.rotation.rotate(Direction.WEST), -6);
      data.rotation = data.rotation.getRotated(Rotation.CLOCKWISE_90);
   }

   private void traverseInnerTurn(final List<WoodlandMansionPieces$WoodlandMansionPiece> pieces, final WoodlandMansionPieces$PlacementData data) {
      data.position = data.position.relative(data.rotation.rotate(Direction.SOUTH), 6);
      data.position = data.position.relative(data.rotation.rotate(Direction.EAST), 8);
      data.rotation = data.rotation.getRotated(Rotation.COUNTERCLOCKWISE_90);
   }

   private void addRoom1x1(
      final List<WoodlandMansionPieces$WoodlandMansionPiece> pieces,
      final BlockPos roomPos,
      final Rotation rotation,
      final Direction doorDir,
      final WoodlandMansionPieces$FloorRoomCollection rooms
   ) {
      Rotation pieceRot = Rotation.NONE;
      String roomType = rooms.get1x1(this.random);
      if (doorDir != Direction.EAST) {
         if (doorDir == Direction.NORTH) {
            pieceRot = pieceRot.getRotated(Rotation.COUNTERCLOCKWISE_90);
         } else if (doorDir == Direction.WEST) {
            pieceRot = pieceRot.getRotated(Rotation.CLOCKWISE_180);
         } else if (doorDir == Direction.SOUTH) {
            pieceRot = pieceRot.getRotated(Rotation.CLOCKWISE_90);
         } else {
            roomType = rooms.get1x1Secret(this.random);
         }
      }

      BlockPos orientation = StructureTemplate.getZeroPositionWithTransform(new BlockPos(1, 0, 0), Mirror.NONE, pieceRot, 7, 7);
      pieceRot = pieceRot.getRotated(rotation);
      orientation = orientation.rotate(rotation);
      BlockPos pos = roomPos.offset(orientation.getX(), 0, orientation.getZ());
      pieces.add(new WoodlandMansionPieces$WoodlandMansionPiece(this.structureTemplateManager, roomType, pos, pieceRot));
   }

   private void addRoom1x2(
      final List<WoodlandMansionPieces$WoodlandMansionPiece> pieces,
      final BlockPos roomPos,
      final Rotation rotation,
      final Direction roomDir,
      final Direction doorDir,
      final WoodlandMansionPieces$FloorRoomCollection rooms,
      final boolean isStairsRoom
   ) {
      if (doorDir == Direction.EAST && roomDir == Direction.SOUTH) {
         BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 1);
         pieces.add(
            new WoodlandMansionPieces$WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2SideEntrance(this.random, isStairsRoom), pos, rotation)
         );
      } else if (doorDir == Direction.EAST && roomDir == Direction.NORTH) {
         BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 1);
         pos = pos.relative(rotation.rotate(Direction.SOUTH), 6);
         pieces.add(
            new WoodlandMansionPieces$WoodlandMansionPiece(
               this.structureTemplateManager, rooms.get1x2SideEntrance(this.random, isStairsRoom), pos, rotation, Mirror.LEFT_RIGHT
            )
         );
      } else if (doorDir == Direction.WEST && roomDir == Direction.NORTH) {
         BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 7);
         pos = pos.relative(rotation.rotate(Direction.SOUTH), 6);
         pieces.add(
            new WoodlandMansionPieces$WoodlandMansionPiece(
               this.structureTemplateManager, rooms.get1x2SideEntrance(this.random, isStairsRoom), pos, rotation.getRotated(Rotation.CLOCKWISE_180)
            )
         );
      } else if (doorDir == Direction.WEST && roomDir == Direction.SOUTH) {
         BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 7);
         pieces.add(
            new WoodlandMansionPieces$WoodlandMansionPiece(
               this.structureTemplateManager, rooms.get1x2SideEntrance(this.random, isStairsRoom), pos, rotation, Mirror.FRONT_BACK
            )
         );
      } else if (doorDir == Direction.SOUTH && roomDir == Direction.EAST) {
         BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 1);
         pieces.add(
            new WoodlandMansionPieces$WoodlandMansionPiece(
               this.structureTemplateManager,
               rooms.get1x2SideEntrance(this.random, isStairsRoom),
               pos,
               rotation.getRotated(Rotation.CLOCKWISE_90),
               Mirror.LEFT_RIGHT
            )
         );
      } else if (doorDir == Direction.SOUTH && roomDir == Direction.WEST) {
         BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 7);
         pieces.add(
            new WoodlandMansionPieces$WoodlandMansionPiece(
               this.structureTemplateManager, rooms.get1x2SideEntrance(this.random, isStairsRoom), pos, rotation.getRotated(Rotation.CLOCKWISE_90)
            )
         );
      } else if (doorDir == Direction.NORTH && roomDir == Direction.WEST) {
         BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 7);
         pos = pos.relative(rotation.rotate(Direction.SOUTH), 6);
         pieces.add(
            new WoodlandMansionPieces$WoodlandMansionPiece(
               this.structureTemplateManager,
               rooms.get1x2SideEntrance(this.random, isStairsRoom),
               pos,
               rotation.getRotated(Rotation.CLOCKWISE_90),
               Mirror.FRONT_BACK
            )
         );
      } else if (doorDir == Direction.NORTH && roomDir == Direction.EAST) {
         BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 1);
         pos = pos.relative(rotation.rotate(Direction.SOUTH), 6);
         pieces.add(
            new WoodlandMansionPieces$WoodlandMansionPiece(
               this.structureTemplateManager, rooms.get1x2SideEntrance(this.random, isStairsRoom), pos, rotation.getRotated(Rotation.COUNTERCLOCKWISE_90)
            )
         );
      } else if (doorDir == Direction.SOUTH && roomDir == Direction.NORTH) {
         BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 1);
         pos = pos.relative(rotation.rotate(Direction.NORTH), 8);
         pieces.add(
            new WoodlandMansionPieces$WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2FrontEntrance(this.random, isStairsRoom), pos, rotation)
         );
      } else if (doorDir == Direction.NORTH && roomDir == Direction.SOUTH) {
         BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 7);
         pos = pos.relative(rotation.rotate(Direction.SOUTH), 14);
         pieces.add(
            new WoodlandMansionPieces$WoodlandMansionPiece(
               this.structureTemplateManager, rooms.get1x2FrontEntrance(this.random, isStairsRoom), pos, rotation.getRotated(Rotation.CLOCKWISE_180)
            )
         );
      } else if (doorDir == Direction.WEST && roomDir == Direction.EAST) {
         BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 15);
         pieces.add(
            new WoodlandMansionPieces$WoodlandMansionPiece(
               this.structureTemplateManager, rooms.get1x2FrontEntrance(this.random, isStairsRoom), pos, rotation.getRotated(Rotation.CLOCKWISE_90)
            )
         );
      } else if (doorDir == Direction.EAST && roomDir == Direction.WEST) {
         BlockPos pos = roomPos.relative(rotation.rotate(Direction.WEST), 7);
         pos = pos.relative(rotation.rotate(Direction.SOUTH), 6);
         pieces.add(
            new WoodlandMansionPieces$WoodlandMansionPiece(
               this.structureTemplateManager, rooms.get1x2FrontEntrance(this.random, isStairsRoom), pos, rotation.getRotated(Rotation.COUNTERCLOCKWISE_90)
            )
         );
      } else if (doorDir == Direction.UP && roomDir == Direction.EAST) {
         BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 15);
         pieces.add(
            new WoodlandMansionPieces$WoodlandMansionPiece(
               this.structureTemplateManager, rooms.get1x2Secret(this.random), pos, rotation.getRotated(Rotation.CLOCKWISE_90)
            )
         );
      } else if (doorDir == Direction.UP && roomDir == Direction.SOUTH) {
         BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 1);
         pos = pos.relative(rotation.rotate(Direction.NORTH), 0);
         pieces.add(new WoodlandMansionPieces$WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2Secret(this.random), pos, rotation));
      }
   }

   private void addRoom2x2(
      final List<WoodlandMansionPieces$WoodlandMansionPiece> pieces,
      final BlockPos roomPos,
      final Rotation rotation,
      final Direction roomDir,
      final Direction doorDir,
      final WoodlandMansionPieces$FloorRoomCollection rooms
   ) {
      int east = 0;
      int south = 0;
      Rotation rot = rotation;
      Mirror mirror = Mirror.NONE;
      if (doorDir == Direction.EAST && roomDir == Direction.SOUTH) {
         east = -7;
      } else if (doorDir == Direction.EAST && roomDir == Direction.NORTH) {
         east = -7;
         south = 6;
         mirror = Mirror.LEFT_RIGHT;
      } else if (doorDir == Direction.NORTH && roomDir == Direction.EAST) {
         east = 1;
         south = 14;
         rot = rotation.getRotated(Rotation.COUNTERCLOCKWISE_90);
      } else if (doorDir == Direction.NORTH && roomDir == Direction.WEST) {
         east = 7;
         south = 14;
         rot = rotation.getRotated(Rotation.COUNTERCLOCKWISE_90);
         mirror = Mirror.LEFT_RIGHT;
      } else if (doorDir == Direction.SOUTH && roomDir == Direction.WEST) {
         east = 7;
         south = -8;
         rot = rotation.getRotated(Rotation.CLOCKWISE_90);
      } else if (doorDir == Direction.SOUTH && roomDir == Direction.EAST) {
         east = 1;
         south = -8;
         rot = rotation.getRotated(Rotation.CLOCKWISE_90);
         mirror = Mirror.LEFT_RIGHT;
      } else if (doorDir == Direction.WEST && roomDir == Direction.NORTH) {
         east = 15;
         south = 6;
         rot = rotation.getRotated(Rotation.CLOCKWISE_180);
      } else if (doorDir == Direction.WEST && roomDir == Direction.SOUTH) {
         east = 15;
         mirror = Mirror.FRONT_BACK;
      }

      BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), east);
      pos = pos.relative(rotation.rotate(Direction.SOUTH), south);
      pieces.add(new WoodlandMansionPieces$WoodlandMansionPiece(this.structureTemplateManager, rooms.get2x2(this.random), pos, rot, mirror));
   }

   private void addRoom2x2Secret(
      final List<WoodlandMansionPieces$WoodlandMansionPiece> pieces,
      final BlockPos roomPos,
      final Rotation rotation,
      final WoodlandMansionPieces$FloorRoomCollection rooms
   ) {
      BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 1);
      pieces.add(new WoodlandMansionPieces$WoodlandMansionPiece(this.structureTemplateManager, rooms.get2x2Secret(this.random), pos, rotation, Mirror.NONE));
   }
}
