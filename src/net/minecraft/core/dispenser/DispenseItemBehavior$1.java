package net.minecraft.core.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.PostSpawnProcessor;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;

class DispenseItemBehavior$1 extends DefaultDispenseItemBehavior {
   @Override
   public ItemStack execute(final BlockSource source, final ItemStack dispensed) {
      Direction direction = source.state().getValue(DispenserBlock.FACING);
      BlockPos pos = source.pos().relative(direction);
      ServerLevel serverLevel = source.level();
      PostSpawnProcessor<ArmorStand> postSpawnConfig = EntityType.appendDefaultStackConfig(
         armorStandx -> armorStandx.setYRot(direction.toYRot()), serverLevel, dispensed, null
      );
      ArmorStand armorStand = EntityTypes.ARMOR_STAND.spawn(serverLevel, postSpawnConfig, pos, EntitySpawnReason.DISPENSER, false, false);
      if (armorStand != null) {
         dispensed.shrink(1);
      }

      return dispensed;
   }
}
