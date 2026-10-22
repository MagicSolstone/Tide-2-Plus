package net.minecraft.commands.arguments.selector;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.util.CompilableString$CommandParserHelper;

class EntitySelector$2 extends CompilableString$CommandParserHelper<EntitySelector> {
   protected EntitySelector parse(final StringReader reader) throws CommandSyntaxException {
      return new EntitySelectorParser(reader, true).parse();
   }

   @Override
   protected String errorMessage(final String original, final CommandSyntaxException exception) {
      return "Invalid selector component: " + original + ": " + exception.getMessage();
   }
}
