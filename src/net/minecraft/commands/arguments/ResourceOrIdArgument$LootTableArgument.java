package net.minecraft.commands.arguments;

import net.minecraft.commands.CommandBuildContext;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.LootTable;

public class ResourceOrIdArgument$LootTableArgument extends ResourceOrIdArgument<LootTable> {
   protected ResourceOrIdArgument$LootTableArgument(final CommandBuildContext context) {
      super(context, Registries.LOOT_TABLE, LootTable.DIRECT_CODEC);
   }
}
