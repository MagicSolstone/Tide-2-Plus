package net.minecraft.core.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gamerules.GameRules;

class DispenseItemBehavior$6 extends OptionalDispenseItemBehavior {
   @Override
   protected ItemStack execute(final BlockSource source, final ItemStack dispensed) {
      ServerLevel level = source.level();
      if (!level.getGameRules().get(GameRules.TNT_EXPLODES)) {
         this.setSuccess(false);
         return dispensed;
      } else {
         BlockPos target = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
         if (SulfurCubeBlockDispenseItemBehavior.dispenseBlock(level, target, dispensed)) {
            return dispensed;
         } else {
            PrimedTnt tnt = new PrimedTnt(level, target.getX() + 0.5, target.getY(), target.getZ() + 0.5, null);
            level.addFreshEntity(tnt);
            level.playSound(null, tnt.getX(), tnt.getY(), tnt.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent(null, GameEvent.ENTITY_PLACE, target);
            dispensed.shrink(1);
            this.setSuccess(true);
            return dispensed;
         }
      }
   }
}
