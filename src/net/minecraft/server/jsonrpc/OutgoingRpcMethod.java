package net.minecraft.server.jsonrpc;

import com.google.gson.JsonElement;
import net.minecraft.server.jsonrpc.api.MethodInfo;
import org.jspecify.annotations.Nullable;

public interface OutgoingRpcMethod<Params, Result> {
   String NOTIFICATION_PREFIX = "notification/";

   MethodInfo<Params, Result> info();

   OutgoingRpcMethod$Attributes attributes();

   @Nullable
   default JsonElement encodeParams(final Params params) {
      return null;
   }

   @Nullable
   default Result decodeResult(final JsonElement result) {
      return null;
   }

   static OutgoingRpcMethod$OutgoingRpcMethodBuilder<Void, Void> notification() {
      return new OutgoingRpcMethod$OutgoingRpcMethodBuilder<>(OutgoingRpcMethod$ParmeterlessNotification::new);
   }

   static <Params> OutgoingRpcMethod$OutgoingRpcMethodBuilder<Params, Void> notificationWithParams() {
      return new OutgoingRpcMethod$OutgoingRpcMethodBuilder<>(OutgoingRpcMethod$Notification::new);
   }

   static <Result> OutgoingRpcMethod$OutgoingRpcMethodBuilder<Void, Result> request() {
      return new OutgoingRpcMethod$OutgoingRpcMethodBuilder<>(OutgoingRpcMethod$ParameterlessMethod::new);
   }

   static <Params, Result> OutgoingRpcMethod$OutgoingRpcMethodBuilder<Params, Result> requestWithParams() {
      return new OutgoingRpcMethod$OutgoingRpcMethodBuilder<>(OutgoingRpcMethod$Method::new);
   }
}
