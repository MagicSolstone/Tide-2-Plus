package net.minecraft.commands.arguments;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.network.chat.Component;

public record MessageArgument$Part(int start, int end, EntitySelector selector) {
   public Component toComponent(final CommandSourceStack sender) throws CommandSyntaxException {
      return EntitySelector.joinNames(this.selector.findEntities(sender));
   }
}
