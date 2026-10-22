package net.minecraft.server.jsonrpc;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.server.jsonrpc.api.MethodInfo;
import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
import net.minecraft.server.jsonrpc.methods.ClientInfo;
import net.minecraft.server.jsonrpc.methods.InvalidParameterJsonRpcException;
import org.jspecify.annotations.Nullable;

public record IncomingRpcMethod$ParameterlessMethod<Params, Result>(
   MethodInfo<Params, Result> info, IncomingRpcMethod$Attributes attributes, IncomingRpcMethod$ParameterlessRpcMethodFunction<Result> supplier
) implements IncomingRpcMethod<Params, Result> {
   @Override
   public JsonElement apply(final MinecraftApi minecraftApi, @Nullable final JsonElement paramsJson, final ClientInfo clientInfo) {
      if (paramsJson == null || paramsJson.isJsonArray() && paramsJson.getAsJsonArray().isEmpty()) {
         if (this.info.params().isPresent()) {
            throw new IllegalArgumentException("Parameterless method unexpectedly has parameter description");
         } else {
            Result result = this.supplier.apply(minecraftApi, clientInfo);
            if (this.info.result().isEmpty()) {
               throw new IllegalStateException("No result codec defined");
            } else {
               return (JsonElement)this.info
                  .result()
                  .get()
                  .schema()
                  .codec()
                  .encodeStart(JsonOps.INSTANCE, result)
                  .getOrThrow(InvalidParameterJsonRpcException::new);
            }
         }
      } else {
         throw new InvalidParameterJsonRpcException("Expected no params, or an empty array");
      }
   }
}
