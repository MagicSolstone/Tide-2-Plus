package net.minecraft.world.item.crafting;

import java.util.Optional;
import net.minecraft.server.level.ServerLevel;

public interface RecipeManager$CachedCheck<I extends RecipeInput, T extends Recipe<I>> {
   Optional<RecipeHolder<T>> getRecipeFor(I input, ServerLevel level);
}
