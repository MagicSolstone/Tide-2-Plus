package net.minecraft.commands.arguments;

import com.google.gson.JsonObject;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;

public class ResourceSelectorArgument$Info<T> implements ArgumentTypeInfo<ResourceSelectorArgument<T>, ResourceSelectorArgument$Info<T>.Template> {
   public void serializeToNetwork(final ResourceSelectorArgument$Info<T>.Template template, final FriendlyByteBuf out) {
      out.writeResourceKey(template.registryKey);
   }

   public ResourceSelectorArgument$Info<T>.Template deserializeFromNetwork(final FriendlyByteBuf in) {
      return new ResourceSelectorArgument$Info$Template(this, in.readRegistryKey());
   }

   public void serializeToJson(final ResourceSelectorArgument$Info<T>.Template template, final JsonObject out) {
      out.addProperty("registry", template.registryKey.identifier().toString());
   }

   public ResourceSelectorArgument$Info<T>.Template unpack(final ResourceSelectorArgument<T> argument) {
      return new ResourceSelectorArgument$Info$Template(this, argument.registryKey);
   }
}
