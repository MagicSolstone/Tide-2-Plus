package net.minecraft.core.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.WitherSkullBlock;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.level.gameevent.GameEvent;

class DispenseItemBehavior$7 extends OptionalDispenseItemBehavior {
   @Override
   protected ItemStack execute(final BlockSource source, final ItemStack dispensed) {
      Level level = source.level();
      Direction direction = source.state().getValue(DispenserBlock.FACING);
      BlockPos target = source.pos().relative(direction);
      if (level.isEmptyBlock(target) && WitherSkullBlock.canSpawnMob(level, target, dispensed)) {
         level.setBlockAndUpdate(
            target, Blocks.WITHER_SKELETON_SKULL.defaultBlockState().setValue(SkullBlock.ROTATION, RotationSegment.convertToSegment(direction))
         );
         level.gameEvent(null, GameEvent.BLOCK_PLACE, target);
         if (level.getBlockEntity(target) instanceof SkullBlockEntity skullBlockEntity) {
            WitherSkullBlock.checkSpawn(level, target, skullBlockEntity);
         }

         dispensed.shrink(1);
         this.setSuccess(true);
      } else {
         this.setSuccess(EquipmentDispenseItemBehavior.dispenseEquipment(source, dispensed));
      }

      return dispensed;
   }
}
