package net.minecraft.server.jsonrpc;

import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
import net.minecraft.server.jsonrpc.methods.ClientInfo;

@FunctionalInterface
public interface IncomingRpcMethod$RpcMethodFunction<Params, Result> {
   Result apply(MinecraftApi api, Params params, ClientInfo clientInfo);
}
