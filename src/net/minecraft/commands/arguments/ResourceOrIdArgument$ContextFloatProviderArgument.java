package net.minecraft.commands.arguments;

import net.minecraft.commands.CommandBuildContext;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProvider;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProviders;

public class ResourceOrIdArgument$ContextFloatProviderArgument extends ResourceOrIdArgument<ContextFloatProvider> {
   protected ResourceOrIdArgument$ContextFloatProviderArgument(final CommandBuildContext context) {
      super(context, Registries.CONTEXT_FLOAT_PROVIDER, ContextFloatProviders.DIRECT_CODEC);
   }
}
