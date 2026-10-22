package net.minecraft.server.jsonrpc;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import java.util.Locale;
import net.minecraft.server.jsonrpc.api.MethodInfo;
import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
import net.minecraft.server.jsonrpc.methods.ClientInfo;
import net.minecraft.server.jsonrpc.methods.EncodeJsonRpcException;
import net.minecraft.server.jsonrpc.methods.InvalidParameterJsonRpcException;
import org.jspecify.annotations.Nullable;

public record IncomingRpcMethod$Method<Params, Result>(
   MethodInfo<Params, Result> info, IncomingRpcMethod$Attributes attributes, IncomingRpcMethod$RpcMethodFunction<Params, Result> function
) implements IncomingRpcMethod<Params, Result> {
   @Override
   public JsonElement apply(final MinecraftApi minecraftApi, @Nullable final JsonElement paramsJson, final ClientInfo clientInfo) {
      if (paramsJson != null && (paramsJson.isJsonArray() || paramsJson.isJsonObject())) {
         if (this.info.params().isEmpty()) {
            throw new IllegalArgumentException("Method defined as having parameters without describing them");
         } else {
            JsonElement paramsJsonElement;
            if (paramsJson.isJsonObject()) {
               String parameterName = this.info.params().get().name();
               JsonElement jsonElement = paramsJson.getAsJsonObject().get(parameterName);
               if (jsonElement == null) {
                  throw new InvalidParameterJsonRpcException(
                     String.format(Locale.ROOT, "Params passed by-name, but expected param [%s] does not exist", parameterName)
                  );
               }

               paramsJsonElement = jsonElement;
            } else {
               JsonArray jsonArray = paramsJson.getAsJsonArray();
               if (jsonArray.isEmpty() || jsonArray.size() > 1) {
                  throw new InvalidParameterJsonRpcException("Expected exactly one element in the params array");
               }

               paramsJsonElement = jsonArray.get(0);
            }

            Params params = (Params)this.info
               .params()
               .get()
               .schema()
               .codec()
               .parse(JsonOps.INSTANCE, paramsJsonElement)
               .getOrThrow(InvalidParameterJsonRpcException::new);
            Result result = this.function.apply(minecraftApi, params, clientInfo);
            if (this.info.result().isEmpty()) {
               throw new IllegalStateException("No result codec defined");
            } else {
               return (JsonElement)this.info.result().get().schema().codec().encodeStart(JsonOps.INSTANCE, result).getOrThrow(EncodeJsonRpcException::new);
            }
         }
      } else {
         throw new InvalidParameterJsonRpcException("Expected params as array or named");
      }
   }
}
