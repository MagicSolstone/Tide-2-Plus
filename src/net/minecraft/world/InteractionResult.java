package net.minecraft.world;

public sealed interface InteractionResult
   permits InteractionResult$Success,
   InteractionResult$Fail,
   InteractionResult$Pass,
   InteractionResult$TryEmptyHandInteraction {
   InteractionResult$Success SUCCESS = new InteractionResult$Success(InteractionResult$SwingSource.PREDICTED, InteractionResult$ItemContext.DEFAULT);
   InteractionResult$Success SUCCESS_SERVER = new InteractionResult$Success(InteractionResult$SwingSource.SERVER_ONLY, InteractionResult$ItemContext.DEFAULT);
   InteractionResult$Success CONSUME = new InteractionResult$Success(InteractionResult$SwingSource.NONE, InteractionResult$ItemContext.DEFAULT);
   InteractionResult$Fail FAIL = new InteractionResult$Fail();
   InteractionResult$Pass PASS = new InteractionResult$Pass();
   InteractionResult$TryEmptyHandInteraction TRY_WITH_EMPTY_HAND = new InteractionResult$TryEmptyHandInteraction();

   default boolean consumesAction() {
      return false;
   }
}
