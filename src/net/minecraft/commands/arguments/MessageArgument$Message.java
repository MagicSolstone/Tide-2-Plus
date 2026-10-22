package net.minecraft.commands.arguments;

import com.google.common.collect.Lists;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.List;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.permissions.Permissions;

public record MessageArgument$Message(String text, MessageArgument$Part[] parts) {
   private Component resolveComponent(final CommandSourceStack sender) throws CommandSyntaxException {
      return this.toComponent(sender, sender.permissions().hasPermission(Permissions.COMMANDS_ENTITY_SELECTORS));
   }

   public Component toComponent(final CommandSourceStack sender, final boolean allowSelectors) throws CommandSyntaxException {
      if (this.parts.length != 0 && allowSelectors) {
         MutableComponent result = Component.literal(this.text.substring(0, this.parts[0].start()));
         int readTo = this.parts[0].start();

         for (MessageArgument$Part part : this.parts) {
            Component component = part.toComponent(sender);
            if (readTo < part.start()) {
               result.append(this.text.substring(readTo, part.start()));
            }

            result.append(component);
            readTo = part.end();
         }

         if (readTo < this.text.length()) {
            result.append(this.text.substring(readTo));
         }

         return result;
      } else {
         return Component.literal(this.text);
      }
   }

   public static MessageArgument$Message parseText(final StringReader reader, final boolean allowSelectors) throws CommandSyntaxException {
      if (reader.getRemainingLength() > 256) {
         throw MessageArgument.TOO_LONG.create(reader.getRemainingLength(), 256);
      } else {
         String text = reader.getRemaining();
         if (!allowSelectors) {
            reader.setCursor(reader.getTotalLength());
            return new MessageArgument$Message(text, new MessageArgument$Part[0]);
         } else {
            List<MessageArgument$Part> result = Lists.newArrayList();
            int offset = reader.getCursor();

            while (true) {
               int start;
               EntitySelector parse;
               while (true) {
                  if (!reader.canRead()) {
                     return new MessageArgument$Message(text, result.toArray(new MessageArgument$Part[0]));
                  }

                  if (reader.peek() == '@') {
                     start = reader.getCursor();

                     try {
                        EntitySelectorParser parser = new EntitySelectorParser(reader, true);
                        parse = parser.parse();
                        break;
                     } catch (CommandSyntaxException var8) {
                        if (var8.getType() != EntitySelectorParser.ERROR_MISSING_SELECTOR_TYPE
                           && var8.getType() != EntitySelectorParser.ERROR_UNKNOWN_SELECTOR_TYPE) {
                           throw var8;
                        }

                        reader.setCursor(start + 1);
                     }
                  } else {
                     reader.skip();
                  }
               }

               result.add(new MessageArgument$Part(start - offset, reader.getCursor() - offset, parse));
            }
         }
      }
   }
}
