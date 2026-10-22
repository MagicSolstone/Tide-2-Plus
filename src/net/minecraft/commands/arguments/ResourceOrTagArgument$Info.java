package net.minecraft.commands.arguments;

import com.google.gson.JsonObject;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;

public class ResourceOrTagArgument$Info<T> implements ArgumentTypeInfo<ResourceOrTagArgument<T>, ResourceOrTagArgument$Info<T>.Template> {
   public void serializeToNetwork(final ResourceOrTagArgument$Info<T>.Template template, final FriendlyByteBuf out) {
      out.writeResourceKey(template.registryKey);
   }

   public ResourceOrTagArgument$Info<T>.Template deserializeFromNetwork(final FriendlyByteBuf in) {
      return new ResourceOrTagArgument$Info$Template(this, in.readRegistryKey());
   }

   public void serializeToJson(final ResourceOrTagArgument$Info<T>.Template template, final JsonObject out) {
      out.addProperty("registry", template.registryKey.identifier().toString());
   }

   public ResourceOrTagArgument$Info<T>.Template unpack(final ResourceOrTagArgument<T> argument) {
      return new ResourceOrTagArgument$Info$Template(this, argument.registryKey);
   }
}
