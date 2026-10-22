package net.minecraft.core.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity$BeeReleaseStatus;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

class DispenseItemBehavior$9 extends OptionalDispenseItemBehavior {
   private ItemStack takeLiquid(final BlockSource source, final ItemStack dispensed, final ItemStack filledItemStack) {
      source.level().gameEvent(null, GameEvent.FLUID_PICKUP, source.pos());
      return this.consumeWithRemainder(source, dispensed, filledItemStack);
   }

   @Override
   public ItemStack execute(final BlockSource source, final ItemStack dispensed) {
      this.setSuccess(false);
      ServerLevel level = source.level();
      BlockPos target = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
      BlockState state = level.getBlockState(target);
      if (state.is(BlockTags.BEEHIVES, s -> s.hasProperty(BeehiveBlock.HONEY_LEVEL) && s.getBlock() instanceof BeehiveBlock)
         && state.getValue(BeehiveBlock.HONEY_LEVEL) >= 5) {
         ((BeehiveBlock)state.getBlock()).releaseBeesAndResetHoneyLevel(level, state, target, null, BeehiveBlockEntity$BeeReleaseStatus.BEE_RELEASED);
         this.setSuccess(true);
         return this.takeLiquid(source, dispensed, new ItemStack(Items.HONEY_BOTTLE));
      } else if (level.getFluidState(target).is(FluidTags.WATER)) {
         this.setSuccess(true);
         return this.takeLiquid(source, dispensed, PotionContents.createItemStack(Items.POTION, Potions.WATER));
      } else {
         return super.execute(source, dispensed);
      }
   }
}
