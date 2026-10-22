package net.minecraft.server.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;

class GameRuleCommand$1 implements GameRuleTypeVisitor {
   GameRuleCommand$1(final LiteralArgumentBuilder val$base) {
      this.val$base = val$basex;
   }

   @Override
   public <T> void visit(final GameRule<T> gameRule) {
      LiteralArgumentBuilder<CommandSourceStack> unqualified = Commands.literal(gameRule.id());
      LiteralArgumentBuilder<CommandSourceStack> qualified = Commands.literal(gameRule.getIdentifier().toString());
      ((LiteralArgumentBuilder)this.val$base.then(GameRuleCommand.buildRuleArguments(gameRule, unqualified)))
         .then(GameRuleCommand.buildRuleArguments(gameRule, qualified));
   }
}
