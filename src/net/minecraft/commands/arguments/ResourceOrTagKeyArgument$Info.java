package net.minecraft.commands.arguments;

import com.google.gson.JsonObject;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;

public class ResourceOrTagKeyArgument$Info<T> implements ArgumentTypeInfo<ResourceOrTagKeyArgument<T>, ResourceOrTagKeyArgument$Info<T>.Template> {
   public void serializeToNetwork(final ResourceOrTagKeyArgument$Info<T>.Template template, final FriendlyByteBuf out) {
      out.writeResourceKey(template.registryKey);
   }

   public ResourceOrTagKeyArgument$Info<T>.Template deserializeFromNetwork(final FriendlyByteBuf in) {
      return new ResourceOrTagKeyArgument$Info$Template(this, in.readRegistryKey());
   }

   public void serializeToJson(final ResourceOrTagKeyArgument$Info<T>.Template template, final JsonObject out) {
      out.addProperty("registry", template.registryKey.identifier().toString());
   }

   public ResourceOrTagKeyArgument$Info<T>.Template unpack(final ResourceOrTagKeyArgument<T> argument) {
      return new ResourceOrTagKeyArgument$Info$Template(this, argument.registryKey);
   }
}
