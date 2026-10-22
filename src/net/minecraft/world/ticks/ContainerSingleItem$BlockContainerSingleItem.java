package net.minecraft.world.ticks;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface ContainerSingleItem$BlockContainerSingleItem extends ContainerSingleItem {
   BlockEntity getContainerBlockEntity();

   @Override
   default boolean stillValid(final Player player) {
      return Container.stillValidBlockEntity(this.getContainerBlockEntity(), player);
   }
}
