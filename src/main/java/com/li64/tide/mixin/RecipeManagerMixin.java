package com.li64.tide.mixin;

import com.li64.tide.data.tackle.TackleRecipes;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeMap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Fishing gear recipes go to the Angling Table: take them out before the crafting table and recipe book are set up. */
@Mixin(RecipeManager.class)
public abstract class RecipeManagerMixin {
    @Shadow @Final @Mutable private RecipeMap recipes;

    @Inject(method = "finalizeRecipeLoading", at = @At("HEAD"))
    private void tide$moveTackleRecipes(FeatureFlagSet flags, CallbackInfo ci) {
        this.recipes = TackleRecipes.split(this.recipes, RecipeMapAccessor::tide$create);
    }
}
