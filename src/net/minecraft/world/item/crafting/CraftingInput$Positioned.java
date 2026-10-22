package net.minecraft.world.item.crafting;

public record CraftingInput$Positioned(CraftingInput input, int left, int top) {
   public static final CraftingInput$Positioned EMPTY = new CraftingInput$Positioned(CraftingInput.EMPTY, 0, 0);
}
