package net.minecraft.commands.arguments;

import java.util.Objects;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfo$Template;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public final class ResourceOrTagKeyArgument$Info$Template implements ArgumentTypeInfo$Template<ResourceOrTagKeyArgument<T>> {
   private final ResourceKey<? extends Registry<T>> registryKey;

   private ResourceOrTagKeyArgument$Info$Template(final ResourceKey<? extends Registry<T>> this$0, final ResourceKey registryKey) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.registryKey = registryKey;
   }

   public ResourceOrTagKeyArgument<T> instantiate(final CommandBuildContext context) {
      return new ResourceOrTagKeyArgument(this.registryKey);
   }

   @Override
   public ArgumentTypeInfo<ResourceOrTagKeyArgument<T>, ?> type() {
      return this.this$0;
   }
}
