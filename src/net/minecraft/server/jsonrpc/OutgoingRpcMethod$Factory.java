package net.minecraft.server.jsonrpc;

import net.minecraft.server.jsonrpc.api.MethodInfo;

@FunctionalInterface
public interface OutgoingRpcMethod$Factory<Params, Result> {
   OutgoingRpcMethod<Params, Result> create(MethodInfo<Params, Result> info, OutgoingRpcMethod$Attributes attributes);
}
