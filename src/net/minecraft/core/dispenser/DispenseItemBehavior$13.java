package net.minecraft.core.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;

class DispenseItemBehavior$13 extends DefaultDispenseItemBehavior {
   private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

   @Override
   public ItemStack execute(final BlockSource source, final ItemStack dispensed) {
      PotionContents potion = dispensed.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
      if (!potion.is(Potions.WATER)) {
         return this.defaultDispenseItemBehavior.dispense(source, dispensed);
      } else {
         ServerLevel level = source.level();
         BlockPos pos = source.pos();
         BlockPos target = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
         if (!level.getBlockState(target).is(BlockTags.CONVERTIBLE_TO_MUD)) {
            return this.defaultDispenseItemBehavior.dispense(source, dispensed);
         } else {
            if (!level.isClientSide()) {
               RandomSource random = level.getRandom();

               for (int i = 0; i < 5; i++) {
                  level.sendParticles(
                     ParticleTypes.SPLASH, pos.getX() + random.nextDouble(), pos.getY() + 1, pos.getZ() + random.nextDouble(), 1, 0.0, 0.0, 0.0, 1.0
                  );
               }
            }

            level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
            level.setBlockAndUpdate(target, Blocks.MUD.defaultBlockState());
            return this.consumeWithRemainder(source, dispensed, new ItemStack(Items.GLASS_BOTTLE));
         }
      }
   }
}
