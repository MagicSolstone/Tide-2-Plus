package net.minecraft.server.jsonrpc;

public record IncomingRpcMethod$Attributes(boolean runOnMainThread, boolean discoverable, boolean allowPreServerInit) {
}
