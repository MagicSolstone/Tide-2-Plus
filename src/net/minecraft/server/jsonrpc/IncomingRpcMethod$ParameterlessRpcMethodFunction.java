package net.minecraft.server.jsonrpc;

import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
import net.minecraft.server.jsonrpc.methods.ClientInfo;

@FunctionalInterface
public interface IncomingRpcMethod$ParameterlessRpcMethodFunction<Result> {
   Result apply(MinecraftApi api, ClientInfo clientInfo);
}
