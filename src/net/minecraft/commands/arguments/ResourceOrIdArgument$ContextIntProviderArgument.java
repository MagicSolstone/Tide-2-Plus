package net.minecraft.commands.arguments;

import net.minecraft.commands.CommandBuildContext;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

public class ResourceOrIdArgument$ContextIntProviderArgument extends ResourceOrIdArgument<ContextIntProvider> {
   protected ResourceOrIdArgument$ContextIntProviderArgument(final CommandBuildContext context) {
      super(context, Registries.CONTEXT_INT_PROVIDER, ContextIntProviders.DIRECT_CODEC);
   }
}
