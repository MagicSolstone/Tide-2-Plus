package net.minecraft.world;

import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record InteractionResult$Success(InteractionResult$SwingSource swingSource, InteractionResult$ItemContext itemContext) implements InteractionResult {
   public boolean shouldSwing() {
      return this.swingSource != InteractionResult$SwingSource.NONE;
   }

   @Override
   public boolean consumesAction() {
      return true;
   }

   public InteractionResult$Success heldItemTransformedTo(final ItemStack itemStack) {
      return new InteractionResult$Success(this.swingSource, new InteractionResult$ItemContext(true, itemStack));
   }

   public InteractionResult$Success withoutItem() {
      return new InteractionResult$Success(this.swingSource, InteractionResult$ItemContext.NONE);
   }

   public boolean wasItemInteraction() {
      return this.itemContext.wasItemInteraction;
   }

   @Nullable
   public ItemStack heldItemTransformedTo() {
      return this.itemContext.heldItemTransformedTo;
   }
}
