package net.minecraft.world.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignTextSlot;

public class GlowInkSacItem extends Item implements SignApplicator {
   public GlowInkSacItem(final Item$Properties properties) {
      super(properties);
   }

   @Override
   public boolean tryApplyToSign(final Level level, final SignBlockEntity sign, final SignTextSlot slot, final ItemStack item, final Player player) {
      if (sign.updateText(text -> text.withGlowingText(true), slot)) {
         level.playSound(null, sign.getBlockPos(), SoundEvents.GLOW_INK_SAC_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
         return true;
      } else {
         return false;
      }
   }
}
