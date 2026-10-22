package net.minecraft.commands.arguments;

import java.util.Objects;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfo$Template;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public final class ResourceArgument$Info$Template implements ArgumentTypeInfo$Template<ResourceArgument<T>> {
   private final ResourceKey<? extends Registry<T>> registryKey;

   private ResourceArgument$Info$Template(final ResourceKey<? extends Registry<T>> this$0, final ResourceKey registryKey) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.registryKey = registryKey;
   }

   public ResourceArgument<T> instantiate(final CommandBuildContext context) {
      return new ResourceArgument(context, this.registryKey);
   }

   @Override
   public ArgumentTypeInfo<ResourceArgument<T>, ?> type() {
      return this.this$0;
   }
}
