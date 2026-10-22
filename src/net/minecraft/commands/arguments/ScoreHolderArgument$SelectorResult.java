package net.minecraft.commands.arguments;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.scores.ScoreHolder;

public class ScoreHolderArgument$SelectorResult implements ScoreHolderArgument$Result {
   private final EntitySelector selector;

   public ScoreHolderArgument$SelectorResult(final EntitySelector selector) {
      this.selector = selector;
   }

   @Override
   public Collection<ScoreHolder> getNames(final CommandSourceStack sender, final Supplier<Collection<ScoreHolder>> wildcard) throws CommandSyntaxException {
      List<? extends Entity> entities = this.selector.findEntities(sender);
      if (entities.isEmpty()) {
         throw EntityArgument.NO_ENTITIES_FOUND.create();
      } else {
         return List.copyOf(entities);
      }
   }
}
