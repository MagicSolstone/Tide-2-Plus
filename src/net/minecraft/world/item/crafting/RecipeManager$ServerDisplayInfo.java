package net.minecraft.world.item.crafting;

import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;

public record RecipeManager$ServerDisplayInfo(RecipeDisplayEntry display, RecipeHolder<?> parent) {
}
