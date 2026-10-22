package net.minecraft.commands.arguments;

import com.google.gson.JsonObject;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;

public class ResourceKeyArgument$Info<T> implements ArgumentTypeInfo<ResourceKeyArgument<T>, ResourceKeyArgument$Info<T>.Template> {
   public void serializeToNetwork(final ResourceKeyArgument$Info<T>.Template template, final FriendlyByteBuf out) {
      out.writeResourceKey(template.registryKey);
   }

   public ResourceKeyArgument$Info<T>.Template deserializeFromNetwork(final FriendlyByteBuf in) {
      return new ResourceKeyArgument$Info$Template(this, in.readRegistryKey());
   }

   public void serializeToJson(final ResourceKeyArgument$Info<T>.Template template, final JsonObject out) {
      out.addProperty("registry", template.registryKey.identifier().toString());
   }

   public ResourceKeyArgument$Info<T>.Template unpack(final ResourceKeyArgument<T> argument) {
      return new ResourceKeyArgument$Info$Template(this, argument.registryKey);
   }
}
