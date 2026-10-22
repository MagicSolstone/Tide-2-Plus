package net.minecraft.world.level.block;

import java.util.Objects;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

class ChestBlock$2$1 implements MenuProvider {
   ChestBlock$2$1(final ChestBlock$2 this$0, final ChestBlockEntity val$first, final ChestBlockEntity val$second, final Container val$container) {
      this.val$first = val$firstx;
      this.val$second = val$secondx;
      this.val$container = val$containerx;
      Objects.requireNonNull(this$0);
      super();
   }

   @Nullable
   @Override
   public AbstractContainerMenu createMenu(final int containerId, final Inventory inventory, final Player player) {
      if (this.val$first.canOpen(player) && this.val$second.canOpen(player)) {
         this.val$first.unpackLootTable(inventory.player);
         this.val$second.unpackLootTable(inventory.player);
         return ChestMenu.sixRows(containerId, inventory, this.val$container);
      } else {
         if (!player.isSpectator()) {
            Direction connectedDirection = ChestBlock.getConnectedDirection(this.val$first.getBlockState());
            Vec3 firstCenter = Vec3.atCenterOf(this.val$first.getBlockPos());
            Vec3 centerBetweenChests = firstCenter.add(connectedDirection.getStepX() / 2.0, 0.0, connectedDirection.getStepZ() / 2.0);
            BaseContainerBlockEntity.sendChestLockedNotifications(centerBetweenChests, player, this.getDisplayName());
         }

         return null;
      }
   }

   @Override
   public Component getDisplayName() {
      if (this.val$first.hasCustomName()) {
         return this.val$first.getDisplayName();
      } else {
         return (Component)(this.val$second.hasCustomName() ? this.val$second.getDisplayName() : Component.translatable("container.chestDouble"));
      }
   }
}
