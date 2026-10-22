package net.minecraft.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

public class EntityAnchorArgument implements ArgumentType<EntityAnchorArgument$Anchor> {
   private static final Collection<String> EXAMPLES = Arrays.asList("eyes", "feet");
   private static final DynamicCommandExceptionType ERROR_INVALID = new DynamicCommandExceptionType(
      name -> Component.translatableEscape("argument.anchor.invalid", name)
   );

   public static EntityAnchorArgument$Anchor getAnchor(final CommandContext<CommandSourceStack> context, final String name) {
      return (EntityAnchorArgument$Anchor)context.getArgument(name, EntityAnchorArgument$Anchor.class);
   }

   public static EntityAnchorArgument anchor() {
      return new EntityAnchorArgument();
   }

   public EntityAnchorArgument$Anchor parse(final StringReader reader) throws CommandSyntaxException {
      int start = reader.getCursor();
      String name = reader.readUnquotedString();
      EntityAnchorArgument$Anchor anchor = EntityAnchorArgument$Anchor.getByName(name);
      if (anchor == null) {
         reader.setCursor(start);
         throw ERROR_INVALID.createWithContext(reader, name);
      } else {
         return anchor;
      }
   }

   public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
      return SharedSuggestionProvider.suggest(EntityAnchorArgument$Anchor.BY_NAME.keySet(), builder);
   }

   public Collection<String> getExamples() {
      return EXAMPLES;
   }
}
