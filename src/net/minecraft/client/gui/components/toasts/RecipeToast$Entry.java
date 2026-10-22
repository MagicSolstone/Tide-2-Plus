package net.minecraft.client.gui.components.toasts;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
record RecipeToast$Entry(ItemStack categoryItem, ItemStack unlockedItem) {
}
