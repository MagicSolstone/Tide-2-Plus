package net.minecraft.world;

import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record InteractionResult$ItemContext(boolean wasItemInteraction, @Nullable ItemStack heldItemTransformedTo) {
   public static final InteractionResult$ItemContext NONE = new InteractionResult$ItemContext(false, null);
   public static final InteractionResult$ItemContext DEFAULT = new InteractionResult$ItemContext(true, null);
}
