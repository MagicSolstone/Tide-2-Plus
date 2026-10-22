package net.minecraft.commands.arguments;

import net.minecraft.commands.CommandBuildContext;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;

public class ResourceOrIdArgument$LootModifierArgument extends ResourceOrIdArgument<LootItemFunction> {
   protected ResourceOrIdArgument$LootModifierArgument(final CommandBuildContext context) {
      super(context, Registries.ITEM_MODIFIER, LootItemFunctions.DIRECT_CODEC);
   }
}
