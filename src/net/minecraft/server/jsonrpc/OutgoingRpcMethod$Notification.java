package net.minecraft.server.jsonrpc;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.server.jsonrpc.api.MethodInfo;
import org.jspecify.annotations.Nullable;

public record OutgoingRpcMethod$Notification<Params>(MethodInfo<Params, Void> info, OutgoingRpcMethod$Attributes attributes)
   implements OutgoingRpcMethod<Params, Void> {
   @Nullable
   @Override
   public JsonElement encodeParams(final Params params) {
      if (this.info.params().isEmpty()) {
         throw new IllegalStateException("Method defined as having no parameters");
      } else {
         return (JsonElement)this.info.params().get().schema().codec().encodeStart(JsonOps.INSTANCE, params).getOrThrow();
      }
   }
}
