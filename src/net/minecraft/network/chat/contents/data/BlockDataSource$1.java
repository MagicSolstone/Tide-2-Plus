package net.minecraft.network.chat.contents.data;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.coordinates.Coordinates;
import net.minecraft.util.CompilableString$CommandParserHelper;

class BlockDataSource$1 extends CompilableString$CommandParserHelper<Coordinates> {
   protected Coordinates parse(final StringReader reader) throws CommandSyntaxException {
      return BlockPosArgument.blockPos().parse(reader);
   }

   @Override
   protected String errorMessage(final String original, final CommandSyntaxException exception) {
      return "Invalid coordinates path: " + original + ": " + exception.getMessage();
   }
}
