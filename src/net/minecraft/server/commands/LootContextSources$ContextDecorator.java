package net.minecraft.server.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Optional;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContext$Builder;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootParams$Builder;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

@FunctionalInterface
public interface LootContextSources$ContextDecorator {
   LootParams customize(CommandContext<CommandSourceStack> context, LootParams$Builder params) throws CommandSyntaxException;

   default LootParams createParams(final CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
      CommandSourceStack source = (CommandSourceStack)context.getSource();
      ServerLevel level = source.getLevel();
      LootParams$Builder commonContext = new LootParams$Builder(level)
         .withOptionalParameter(LootContextParams.THIS_ENTITY, source.getEntity())
         .withParameter(LootContextParams.ORIGIN, source.getPosition());
      return this.customize(context, commonContext);
   }

   default LootContext createContext(final CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
      LootParams params = this.createParams(context);
      return new LootContext$Builder(params).create(Optional.empty());
   }
}
