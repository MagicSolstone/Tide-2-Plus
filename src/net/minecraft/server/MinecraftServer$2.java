package net.minecraft.server;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;
import net.minecraft.world.level.gamerules.GameRules;

class MinecraftServer$2 implements GameRuleTypeVisitor {
   MinecraftServer$2(final MinecraftServer this$0, final List val$entries, final GameRules val$gameRules) {
      this.val$entries = val$entriesx;
      this.val$gameRules = val$gameRulesx;
      Objects.requireNonNull(this$0);
      super();
   }

   @Override
   public <T> void visit(final GameRule<T> gameRule) {
      this.val$entries.add(String.format(Locale.ROOT, "%s=%s\n", gameRule.getIdentifier(), this.val$gameRules.getAsString(gameRule)));
   }
}
