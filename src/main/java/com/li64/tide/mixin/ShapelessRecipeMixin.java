package com.li64.tide.mixin;

import com.li64.tide.data.rods.RodEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Upgrading a hook (amethyst, diamond...) remembers the hook it was made on, so a bone hook's perks carry over. */
@Mixin(ShapelessRecipe.class)
public abstract class ShapelessRecipeMixin {
    @Inject(method = "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;)Lnet/minecraft/world/item/ItemStack;", at = @At("RETURN"))
    private void tide$keepBaseHook(CraftingInput input, CallbackInfoReturnable<ItemStack> cir) {
        RodEffects.onHookCrafted(input.items(), cir.getReturnValue());
    }
}
