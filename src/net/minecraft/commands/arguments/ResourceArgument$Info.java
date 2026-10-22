package net.minecraft.commands.arguments;

import com.google.gson.JsonObject;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;

public class ResourceArgument$Info<T> implements ArgumentTypeInfo<ResourceArgument<T>, ResourceArgument$Info<T>.Template> {
   public void serializeToNetwork(final ResourceArgument$Info<T>.Template template, final FriendlyByteBuf out) {
      out.writeResourceKey(template.registryKey);
   }

   public ResourceArgument$Info<T>.Template deserializeFromNetwork(final FriendlyByteBuf in) {
      return new ResourceArgument$Info$Template(this, in.readRegistryKey());
   }

   public void serializeToJson(final ResourceArgument$Info<T>.Template template, final JsonObject out) {
      out.addProperty("registry", template.registryKey.identifier().toString());
   }

   public ResourceArgument$Info<T>.Template unpack(final ResourceArgument<T> argument) {
      return new ResourceArgument$Info$Template(this, argument.registryKey);
   }
}
