package net.minecraft.world.entity.vehicle.minecart;

import java.util.Objects;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.world.level.BaseCommandBlock;

class MinecartCommandBlock$MinecartCommandBase extends BaseCommandBlock {
   private MinecartCommandBlock$MinecartCommandBase(final MinecartCommandBlock this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
   }

   @Override
   public void onUpdated(final ServerLevel level) {
      this.this$0.getEntityData().set(MinecartCommandBlock.DATA_ID_COMMAND_NAME, this.getCommand());
      this.this$0.getEntityData().set(MinecartCommandBlock.DATA_ID_LAST_OUTPUT, this.getLastOutput());
   }

   @Override
   public CommandSourceStack createCommandSourceStack(final ServerLevel level, final CommandSource source) {
      return new CommandSourceStack(
         source, this.this$0.position(), this.this$0.getRotationVector(), level, LevelBasedPermissionSet.GAMEMASTER, level.getServer(), this.this$0
      );
   }

   @Override
   public boolean isValid() {
      return !this.this$0.isRemoved();
   }
}
