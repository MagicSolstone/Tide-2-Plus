package net.minecraft.commands;

import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import java.util.function.Predicate;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.network.protocol.game.ClientboundCommandsPacket$NodeInspector;
import net.minecraft.resources.Identifier;
import net.minecraft.server.permissions.PermissionSet;
import org.jspecify.annotations.Nullable;

class Commands$1 implements ClientboundCommandsPacket$NodeInspector<CommandSourceStack> {
   private final CommandSourceStack noPermissionSource = Commands.createCompilationContext(PermissionSet.NO_PERMISSIONS);

   @Nullable
   @Override
   public Identifier suggestionId(final ArgumentCommandNode<CommandSourceStack, ?> node) {
      SuggestionProvider<CommandSourceStack> suggestionProvider = node.getCustomSuggestions();
      return suggestionProvider != null ? SuggestionProviders.getName(suggestionProvider) : null;
   }

   @Override
   public boolean isExecutable(final CommandNode<CommandSourceStack> node) {
      return node.getCommand() != null;
   }

   @Override
   public boolean isRestricted(final CommandNode<CommandSourceStack> node) {
      Predicate<CommandSourceStack> requirement = node.getRequirement();
      return !requirement.test(this.noPermissionSource);
   }
}
