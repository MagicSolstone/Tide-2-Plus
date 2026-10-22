package net.minecraft.server.jsonrpc.methods;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
import net.minecraft.world.level.gamerules.GameRule;

public class GameRulesService {
   public static List<GameRulesService$GameRuleUpdate<?>> get(final MinecraftApi minecraftApi) {
      List<GameRulesService$GameRuleUpdate<?>> rules = new ArrayList<>();
      minecraftApi.gameRuleService().getAvailableGameRules().forEach(gameRule -> addGameRule(minecraftApi, (GameRule<?>)gameRule, rules));
      return rules;
   }

   private static <T> void addGameRule(final MinecraftApi minecraftApi, final GameRule<T> gameRule, final List<GameRulesService$GameRuleUpdate<?>> rules) {
      T value = minecraftApi.gameRuleService().getRuleValue(gameRule);
      rules.add(getTypedRule(minecraftApi, gameRule, value));
   }

   public static <T> GameRulesService$GameRuleUpdate<T> getTypedRule(final MinecraftApi minecraftApi, final GameRule<T> gameRule, final T value) {
      return minecraftApi.gameRuleService().getTypedRule(gameRule, value);
   }

   public static <T> GameRulesService$GameRuleUpdate<T> update(
      final MinecraftApi minecraftApi, final GameRulesService$GameRuleUpdate<T> update, final ClientInfo clientInfo
   ) {
      return minecraftApi.gameRuleService().updateGameRule(update, clientInfo);
   }
}
