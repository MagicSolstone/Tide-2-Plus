package net.minecraft.world.level.block.entity;

import java.util.Objects;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.world.level.BaseCommandBlock;
import net.minecraft.world.level.block.CommandBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

class CommandBlockEntity$1 extends BaseCommandBlock {
   CommandBlockEntity$1(final CommandBlockEntity this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public void setCommand(final String command) {
      super.setCommand(command);
      this.this$0.setChanged();
   }

   @Override
   public void onUpdated(final ServerLevel level) {
      BlockState state = level.getBlockState(this.this$0.worldPosition);
      level.sendBlockUpdated(this.this$0.worldPosition, state, state, 3);
   }

   @Override
   public CommandSourceStack createCommandSourceStack(final ServerLevel level, final CommandSource source) {
      Direction facing = this.this$0.getBlockState().getValue(CommandBlock.FACING);
      return new CommandSourceStack(
         source,
         Vec3.atCenterOf(this.this$0.worldPosition),
         new Vec2(0.0F, facing.toYRot()),
         level,
         LevelBasedPermissionSet.GAMEMASTER,
         this.getName(),
         level.getServer()
      );
   }

   @Override
   public boolean isValid() {
      return !this.this$0.isRemoved();
   }
}
