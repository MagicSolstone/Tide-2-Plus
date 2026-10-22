package net.minecraft.server;

import java.util.Set;
import net.minecraft.locale.Language;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;

class Bootstrap$1 implements GameRuleTypeVisitor {
   Bootstrap$1(final Language val$language, final Set val$missing) {
      this.val$language = val$languagex;
      this.val$missing = val$missingx;
   }

   @Override
   public <T> void visit(final GameRule<T> gameRule) {
      if (!this.val$language.has(gameRule.getDescriptionId())) {
         this.val$missing.add(gameRule.id());
      }
   }
}
