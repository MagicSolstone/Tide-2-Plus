package net.minecraft.server.jsonrpc;

import net.minecraft.core.Holder$Reference;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.jsonrpc.api.MethodInfo;
import net.minecraft.server.jsonrpc.api.ParamInfo;
import net.minecraft.server.jsonrpc.api.ResultInfo;
import net.minecraft.server.jsonrpc.api.Schema;
import org.jspecify.annotations.Nullable;

public class OutgoingRpcMethod$OutgoingRpcMethodBuilder<Params, Result> {
   public static final OutgoingRpcMethod$Attributes DEFAULT_ATTRIBUTES = new OutgoingRpcMethod$Attributes(true, false);
   private final OutgoingRpcMethod$Factory<Params, Result> method;
   private String description = "";
   @Nullable
   private ParamInfo<Params> paramInfo;
   @Nullable
   private ResultInfo<Result> resultInfo;
   private boolean allowPreServerInit = false;

   public OutgoingRpcMethod$OutgoingRpcMethodBuilder(final OutgoingRpcMethod$Factory<Params, Result> method) {
      this.method = method;
   }

   public OutgoingRpcMethod$OutgoingRpcMethodBuilder<Params, Result> description(final String description) {
      this.description = description;
      return this;
   }

   public OutgoingRpcMethod$OutgoingRpcMethodBuilder<Params, Result> response(final String resultName, final Schema<Result> resultSchema) {
      this.resultInfo = new ResultInfo<>(resultName, resultSchema);
      return this;
   }

   public OutgoingRpcMethod$OutgoingRpcMethodBuilder<Params, Result> param(final String paramName, final Schema<Params> paramSchema) {
      this.paramInfo = new ParamInfo<>(paramName, paramSchema);
      return this;
   }

   public OutgoingRpcMethod$OutgoingRpcMethodBuilder<Params, Result> allowPreServerInit() {
      this.allowPreServerInit = true;
      return this;
   }

   private OutgoingRpcMethod<Params, Result> build() {
      MethodInfo<Params, Result> methodInfo = new MethodInfo<>(this.description, this.paramInfo, this.resultInfo);
      OutgoingRpcMethod$Attributes attributes;
      if (this.allowPreServerInit) {
         attributes = new OutgoingRpcMethod$Attributes(DEFAULT_ATTRIBUTES.discoverable(), true);
      } else {
         attributes = DEFAULT_ATTRIBUTES;
      }

      return this.method.create(methodInfo, attributes);
   }

   public Holder$Reference<OutgoingRpcMethod<Params, Result>> register(final String key) {
      return this.register(Identifier.withDefaultNamespace("notification/" + key));
   }

   private Holder$Reference<OutgoingRpcMethod<Params, Result>> register(final Identifier id) {
      return Registry.registerForHolder(BuiltInRegistries.OUTGOING_RPC_METHOD, id, this.build());
   }
}
