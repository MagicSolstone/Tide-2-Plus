package net.minecraft.server.jsonrpc;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.server.jsonrpc.api.MethodInfo;

public record OutgoingRpcMethod$ParameterlessMethod<Result>(MethodInfo<Void, Result> info, OutgoingRpcMethod$Attributes attributes)
   implements OutgoingRpcMethod<Void, Result> {
   @Override
   public Result decodeResult(final JsonElement result) {
      if (this.info.result().isEmpty()) {
         throw new IllegalStateException("Method defined as having no result");
      } else {
         return (Result)this.info.result().get().schema().codec().parse(JsonOps.INSTANCE, result).getOrThrow();
      }
   }
}
