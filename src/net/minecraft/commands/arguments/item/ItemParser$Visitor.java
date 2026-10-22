package net.minecraft.commands.arguments.item;

import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.Item;

public interface ItemParser$Visitor {
   default void visitItem(final Holder<Item> item) {
   }

   default <T> void visitComponent(final DataComponentType<T> type, final T value) {
   }

   default <T> void visitRemovedComponent(final DataComponentType<T> type) {
   }

   default void visitSuggestions(final Function<SuggestionsBuilder, CompletableFuture<Suggestions>> suggestions) {
   }
}
