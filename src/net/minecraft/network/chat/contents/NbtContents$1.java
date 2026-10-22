package net.minecraft.network.chat.contents;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.NbtPathArgument;
import net.minecraft.commands.arguments.NbtPathArgument$NbtPath;
import net.minecraft.util.CompilableString$CommandParserHelper;

class NbtContents$1 extends CompilableString$CommandParserHelper<NbtPathArgument$NbtPath> {
   protected NbtPathArgument$NbtPath parse(final StringReader reader) throws CommandSyntaxException {
      return new NbtPathArgument().parse(reader);
   }

   @Override
   protected String errorMessage(final String original, final CommandSyntaxException exception) {
      return "Invalid NBT path: " + original + ": " + exception.getMessage();
   }
}
