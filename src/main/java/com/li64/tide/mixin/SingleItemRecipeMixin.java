package com.li64.tide.mixin;

import com.li64.tide.data.angler.CookedFish;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Furnace, smoker and campfire results go through here: a graded fish cooks into a graded meal. */
@Mixin(SingleItemRecipe.class)
public abstract class SingleItemRecipeMixin {
    @Inject(method = "assemble(Lnet/minecraft/world/item/crafting/SingleRecipeInput;)Lnet/minecraft/world/item/ItemStack;", at = @At("RETURN"))
    private void tide$keepFishQuality(SingleRecipeInput input, CallbackInfoReturnable<ItemStack> cir) {
        CookedFish.cook(input.item(), cir.getReturnValue());
    }
}
