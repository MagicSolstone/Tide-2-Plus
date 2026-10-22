package net.minecraft.server.jsonrpc;

import com.google.gson.JsonElement;
import java.util.function.Function;
import net.minecraft.server.jsonrpc.api.MethodInfo;
import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
import net.minecraft.server.jsonrpc.methods.ClientInfo;
import org.jspecify.annotations.Nullable;

public interface IncomingRpcMethod<Params, Result> {
   MethodInfo<Params, Result> info();

   IncomingRpcMethod$Attributes attributes();

   JsonElement apply(MinecraftApi minecraftApi, @Nullable JsonElement paramsJson, ClientInfo clientInfo);

   static <Result> IncomingRpcMethod$IncomingRpcMethodBuilder<Void, Result> method(final IncomingRpcMethod$ParameterlessRpcMethodFunction<Result> function) {
      return new IncomingRpcMethod$IncomingRpcMethodBuilder<>(function);
   }

   static <Params, Result> IncomingRpcMethod$IncomingRpcMethodBuilder<Params, Result> method(final IncomingRpcMethod$RpcMethodFunction<Params, Result> function) {
      return new IncomingRpcMethod$IncomingRpcMethodBuilder<>(function);
   }

   static <Result> IncomingRpcMethod$IncomingRpcMethodBuilder<Void, Result> method(final Function<MinecraftApi, Result> supplier) {
      return new IncomingRpcMethod$IncomingRpcMethodBuilder<>(supplier);
   }
}
