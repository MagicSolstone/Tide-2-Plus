package com.li64.tide.mixin;

import com.google.common.collect.Multimap;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeMap;
import net.minecraft.world.item.crafting.RecipeType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Mixin(RecipeMap.class)
public interface RecipeMapAccessor {
    @Invoker("<init>")
    static RecipeMap tide$create(Multimap<RecipeType<?>, RecipeHolder<?>> byType, Map<ResourceKey<Recipe<?>>, RecipeHolder<?>> byKey) {
        throw new AssertionError();
    }
}
