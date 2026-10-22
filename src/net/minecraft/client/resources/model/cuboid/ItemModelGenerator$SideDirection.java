package net.minecraft.client.resources.model.cuboid;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Direction;

@Environment(EnvType.CLIENT)
enum ItemModelGenerator$SideDirection {
   UP(Direction.UP),
   DOWN(Direction.DOWN),
   LEFT(Direction.EAST),
   RIGHT(Direction.WEST);

   private final Direction direction;

   private ItemModelGenerator$SideDirection(final Direction direction) {
      this.direction = direction;
   }

   public Direction getDirection() {
      return this.direction;
   }

   private boolean isHorizontal() {
      return this == DOWN || this == UP;
   }
}
