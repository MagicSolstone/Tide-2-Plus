package net.minecraft.world.level.gamerules;

import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;

public class GameRuleMap$Builder {
   private final Reference2ObjectMap<GameRule<?>, Object> map = new Reference2ObjectOpenHashMap();

   public <T> GameRuleMap$Builder set(final GameRule<T> gameRule, final T value) {
      this.map.put(gameRule, value);
      return this;
   }

   public GameRuleMap build() {
      return new GameRuleMap(this.map);
   }
}
