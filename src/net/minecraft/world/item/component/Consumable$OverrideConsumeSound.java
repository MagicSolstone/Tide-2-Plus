package net.minecraft.world.item.component;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;

public interface Consumable$OverrideConsumeSound {
   SoundEvent getConsumeSound(final ItemStack itemStack);
}
