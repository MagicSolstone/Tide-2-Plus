package net.minecraft.commands.arguments;

import net.minecraft.commands.CommandBuildContext;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class ResourceOrIdArgument$LootPredicateArgument extends ResourceOrIdArgument<LootItemCondition> {
   protected ResourceOrIdArgument$LootPredicateArgument(final CommandBuildContext context) {
      super(context, Registries.PREDICATE, LootItemCondition.DIRECT_CODEC);
   }
}
