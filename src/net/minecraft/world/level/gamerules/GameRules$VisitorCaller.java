package net.minecraft.world.level.gamerules;

public interface GameRules$VisitorCaller<T> {
   void call(GameRuleTypeVisitor visitor, GameRule<T> key);
}
