package net.minecraft.server.jsonrpc;

import net.minecraft.server.jsonrpc.api.MethodInfo;

public record OutgoingRpcMethod$ParmeterlessNotification(MethodInfo<Void, Void> info, OutgoingRpcMethod$Attributes attributes)
   implements OutgoingRpcMethod<Void, Void> {
}
