package net.minecraft.world.item;

public interface TooltipFlag {
   TooltipFlag$Default NORMAL = new TooltipFlag$Default(false, false);
   TooltipFlag$Default ADVANCED = new TooltipFlag$Default(true, false);

   boolean isAdvanced();

   boolean isCreative();
}
