package net.minecraft.commands.arguments;

import java.util.Objects;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfo$Template;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public final class ResourceSelectorArgument$Info$Template implements ArgumentTypeInfo$Template<ResourceSelectorArgument<T>> {
   private final ResourceKey<? extends Registry<T>> registryKey;

   private ResourceSelectorArgument$Info$Template(final ResourceKey<? extends Registry<T>> this$0, final ResourceKey registryKey) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.registryKey = registryKey;
   }

   public ResourceSelectorArgument<T> instantiate(final CommandBuildContext context) {
      return new ResourceSelectorArgument(context, this.registryKey);
   }

   @Override
   public ArgumentTypeInfo<ResourceSelectorArgument<T>, ?> type() {
      return this.this$0;
   }
}
