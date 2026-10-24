package com.li64.tide.mixin;

import com.li64.tide.registries.items.TideFishingRodItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow public abstract Item getItem();
    @Shadow public abstract int getDamageValue();
    @Shadow public abstract int getMaxDamage();

    /** Tide's rods never break: they stop one short, broken but whole, until they're repaired (string at an anvil). */
    @ModifyVariable(at = @At("HEAD"), argsOnly = true, ordinal = 0,
            method = "applyDamage(ILnet/minecraft/server/level/ServerPlayer;Ljava/util/function/Consumer;)V")
    private int tide$rodsDontBreak(int newDamage) {
        if (getItem() instanceof TideFishingRodItem && getMaxDamage() > 1) return Math.min(newDamage, getMaxDamage() - 1);
        return newDamage;
    }

    /** (Other rods: when one does break, its line, hook, bobber and lure drop.) */
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;setDamageValue(I)V", shift = At.Shift.AFTER),
            method = "applyDamage(ILnet/minecraft/server/level/ServerPlayer;Ljava/util/function/Consumer;)V")
    public void hurtAndBreak(int newDamage, ServerPlayer player, Consumer<ItemStack> onBreak, CallbackInfo ci) {
        if (player != null && getDamageValue() >= getMaxDamage() && getItem() instanceof TideFishingRodItem rod) rod.onItemBroken((ItemStack) (Object) this, player);
    }
}
