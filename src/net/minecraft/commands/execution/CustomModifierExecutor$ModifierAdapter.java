package net.minecraft.commands.execution;

import com.mojang.brigadier.RedirectModifier;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Collection;

public interface CustomModifierExecutor$ModifierAdapter<T> extends CustomModifierExecutor<T>, RedirectModifier<T> {
   default Collection<T> apply(final CommandContext<T> context) throws CommandSyntaxException {
      throw new UnsupportedOperationException("This function should not run");
   }
}
