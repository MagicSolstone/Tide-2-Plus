package net.minecraft.server.jsonrpc.methods;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleType;

public record GameRulesService$GameRuleUpdate<T>(GameRule<T> gameRule, T value) {
   public static final Codec<GameRulesService$GameRuleUpdate<?>> TYPED_CODEC = BuiltInRegistries.GAME_RULE
      .byNameCodec()
      .dispatch("key", GameRulesService$GameRuleUpdate::gameRule, GameRulesService$GameRuleUpdate::getValueAndTypeCodec);
   public static final Codec<GameRulesService$GameRuleUpdate<?>> CODEC = BuiltInRegistries.GAME_RULE
      .byNameCodec()
      .dispatch("key", GameRulesService$GameRuleUpdate::gameRule, GameRulesService$GameRuleUpdate::getValueCodec);

   private static <T> MapCodec<? extends GameRulesService$GameRuleUpdate<T>> getValueCodec(final GameRule<T> gameRule) {
      return gameRule.valueCodec()
         .fieldOf("value")
         .xmap(value -> new GameRulesService$GameRuleUpdate<>(gameRule, (T)value), GameRulesService$GameRuleUpdate::value);
   }

   private static <T> MapCodec<? extends GameRulesService$GameRuleUpdate<T>> getValueAndTypeCodec(final GameRule<T> gameRule) {
      return RecordCodecBuilder.mapCodec(
         i -> i.group(
               StringRepresentable.fromEnum(GameRuleType::values).fieldOf("type").forGetter(r -> r.gameRule.gameRuleType()),
               gameRule.valueCodec().fieldOf("value").forGetter(GameRulesService$GameRuleUpdate::value)
            )
            .apply(i, (type, value) -> getUntypedRule(gameRule, type, (T)value))
      );
   }

   private static <T> GameRulesService$GameRuleUpdate<T> getUntypedRule(final GameRule<T> gameRule, final GameRuleType readType, final T value) {
      if (gameRule.gameRuleType() != readType) {
         throw new InvalidParameterJsonRpcException(
            "Stated type \"" + readType + "\" mismatches with actual type \"" + gameRule.gameRuleType() + "\" of gamerule \"" + gameRule.id() + "\""
         );
      } else {
         return new GameRulesService$GameRuleUpdate<>(gameRule, value);
      }
   }
}
