package net.minecraft.commands.arguments;

import java.util.Objects;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfo$Template;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public final class ResourceKeyArgument$Info$Template implements ArgumentTypeInfo$Template<ResourceKeyArgument<T>> {
   private final ResourceKey<? extends Registry<T>> registryKey;

   private ResourceKeyArgument$Info$Template(final ResourceKey<? extends Registry<T>> this$0, final ResourceKey registryKey) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.registryKey = registryKey;
   }

   public ResourceKeyArgument<T> instantiate(final CommandBuildContext context) {
      return new ResourceKeyArgument(this.registryKey);
   }

   @Override
   public ArgumentTypeInfo<ResourceKeyArgument<T>, ?> type() {
      return this.this$0;
   }
}
