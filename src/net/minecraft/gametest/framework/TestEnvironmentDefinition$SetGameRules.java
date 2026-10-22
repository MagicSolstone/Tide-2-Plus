package net.minecraft.gametest.framework;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleMap;
import net.minecraft.world.level.gamerules.GameRules;

public record TestEnvironmentDefinition$SetGameRules(GameRuleMap gameRulesMap) implements TestEnvironmentDefinition<GameRuleMap> {
   public static final MapCodec<TestEnvironmentDefinition$SetGameRules> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(GameRuleMap.CODEC.fieldOf("rules").forGetter(TestEnvironmentDefinition$SetGameRules::gameRulesMap))
         .apply(i, TestEnvironmentDefinition$SetGameRules::new)
   );

   public GameRuleMap setup(final ServerLevel level) {
      GameRuleMap originalState = GameRuleMap.of();
      GameRules gameRules = level.getGameRules();
      this.gameRulesMap.keySet().forEach(rule -> setFromActive(originalState, (GameRule<?>)rule, gameRules));
      gameRules.setAll(this.gameRulesMap, level.getServer());
      return originalState;
   }

   private static <T> void setFromActive(final GameRuleMap map, final GameRule<T> rule, final GameRules rules) {
      map.set(rule, rules.get(rule));
   }

   public void teardown(final ServerLevel level, final GameRuleMap saveData) {
      level.getGameRules().setAll(saveData, level.getServer());
   }

   @Override
   public MapCodec<TestEnvironmentDefinition$SetGameRules> codec() {
      return CODEC;
   }
}
