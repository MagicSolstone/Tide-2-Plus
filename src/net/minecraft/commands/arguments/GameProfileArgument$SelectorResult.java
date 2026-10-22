package net.minecraft.commands.arguments;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.NameAndId;

public class GameProfileArgument$SelectorResult implements GameProfileArgument$Result {
   private final EntitySelector selector;

   public GameProfileArgument$SelectorResult(final EntitySelector selector) {
      this.selector = selector;
   }

   @Override
   public Collection<NameAndId> getNames(final CommandSourceStack sender) throws CommandSyntaxException {
      List<ServerPlayer> players = this.selector.findPlayers(sender);
      if (players.isEmpty()) {
         throw EntityArgument.NO_PLAYERS_FOUND.create();
      } else {
         List<NameAndId> result = new ArrayList<>();

         for (ServerPlayer entity : players) {
            result.add(entity.nameAndId());
         }

         return result;
      }
   }
}
