package net.minecraft.commands.arguments;

import net.minecraft.commands.CommandBuildContext;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;

public class ResourceOrIdArgument$FeatureArgument extends ResourceOrIdArgument<Feature> {
   protected ResourceOrIdArgument$FeatureArgument(final CommandBuildContext context) {
      super(context, Registries.FEATURE, Feature.DIRECT_CODEC);
   }
}
