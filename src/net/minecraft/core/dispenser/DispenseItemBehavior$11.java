package net.minecraft.core.dispenser;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.animal.armadillo.Armadillo;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.AABB;

class DispenseItemBehavior$11 extends OptionalDispenseItemBehavior {
   @Override
   protected ItemStack execute(final BlockSource source, final ItemStack dispensed) {
      ServerLevel level = source.level();
      BlockPos pos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
      List<Armadillo> armadillos = level.getEntitiesOfClass(Armadillo.class, new AABB(pos), EntitySelector.NO_SPECTATORS);
      if (armadillos.isEmpty()) {
         this.setSuccess(false);
         return dispensed;
      } else {
         for (Armadillo armadillo : armadillos) {
            if (armadillo.brushOffScute(null, dispensed)) {
               dispensed.hurtAndBreak(16, level, null, item -> {});
               return dispensed;
            }
         }

         this.setSuccess(false);
         return dispensed;
      }
   }
}
