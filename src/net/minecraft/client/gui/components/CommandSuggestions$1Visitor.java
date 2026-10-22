package net.minecraft.client.gui.components;

import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.context.ParsedArgument;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.commands.ArgumentVisitor$Output;
import net.minecraft.commands.arguments.MessageArgument$Message;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class CommandSuggestions$1Visitor implements ArgumentVisitor$Output<ClientSuggestionProvider> {
   private boolean foundMessageArgument;

   @Override
   public <T> void accept(
      final CommandContextBuilder<ClientSuggestionProvider> context,
      final ArgumentCommandNode<ClientSuggestionProvider, T> argument,
      @Nullable final ParsedArgument<ClientSuggestionProvider, T> value
   ) {
      this.foundMessageArgument = this.foundMessageArgument | (value != null && value.getResult() instanceof MessageArgument$Message);
   }
}
