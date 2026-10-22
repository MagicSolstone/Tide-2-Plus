package net.minecraft.server.advancements;

import net.minecraft.advancements.AdvancementNode;

@FunctionalInterface
public interface AdvancementVisibilityEvaluator$Output {
   void accept(AdvancementNode advancement, boolean visible);
}
