package net.minecraft.server.jsonrpc;

import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.server.jsonrpc.api.MethodInfo;
import net.minecraft.server.jsonrpc.api.ParamInfo;
import net.minecraft.server.jsonrpc.api.ResultInfo;
import net.minecraft.server.jsonrpc.api.Schema;
import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
import org.jspecify.annotations.Nullable;

public class IncomingRpcMethod$IncomingRpcMethodBuilder<Params, Result> {
   private String description = "";
   @Nullable
   private ParamInfo<Params> paramInfo;
   @Nullable
   private ResultInfo<Result> resultInfo;
   private boolean discoverable = true;
   private boolean runOnMainThread = true;
   @Nullable
   private IncomingRpcMethod$ParameterlessRpcMethodFunction<Result> parameterlessFunction;
   @Nullable
   private IncomingRpcMethod$RpcMethodFunction<Params, Result> parameterFunction;
   private boolean allowPreServerInit = false;

   public IncomingRpcMethod$IncomingRpcMethodBuilder(final IncomingRpcMethod$ParameterlessRpcMethodFunction<Result> function) {
      this.parameterlessFunction = function;
   }

   public IncomingRpcMethod$IncomingRpcMethodBuilder(final IncomingRpcMethod$RpcMethodFunction<Params, Result> function) {
      this.parameterFunction = function;
   }

   public IncomingRpcMethod$IncomingRpcMethodBuilder(final Function<MinecraftApi, Result> supplier) {
      this.parameterlessFunction = (apiService, var2) -> supplier.apply(apiService);
   }

   public IncomingRpcMethod$IncomingRpcMethodBuilder<Params, Result> description(final String description) {
      this.description = description;
      return this;
   }

   public IncomingRpcMethod$IncomingRpcMethodBuilder<Params, Result> response(final String resultName, final Schema<Result> resultSchema) {
      this.resultInfo = new ResultInfo<>(resultName, resultSchema.info());
      return this;
   }

   public IncomingRpcMethod$IncomingRpcMethodBuilder<Params, Result> param(final String paramName, final Schema<Params> paramSchema) {
      this.paramInfo = new ParamInfo<>(paramName, paramSchema.info());
      return this;
   }

   public IncomingRpcMethod$IncomingRpcMethodBuilder<Params, Result> undiscoverable() {
      this.discoverable = false;
      return this;
   }

   public IncomingRpcMethod$IncomingRpcMethodBuilder<Params, Result> notOnMainThread() {
      this.runOnMainThread = false;
      return this;
   }

   public IncomingRpcMethod$IncomingRpcMethodBuilder<Params, Result> allowPreServerInit() {
      this.allowPreServerInit = true;
      return this;
   }

   public IncomingRpcMethod<Params, Result> build() {
      if (this.resultInfo == null) {
         throw new IllegalStateException("No response defined");
      } else {
         IncomingRpcMethod$Attributes attributes = new IncomingRpcMethod$Attributes(this.runOnMainThread, this.discoverable, this.allowPreServerInit);
         MethodInfo<Params, Result> methodInfo = new MethodInfo<>(this.description, this.paramInfo, this.resultInfo);
         if (this.parameterlessFunction != null) {
            return new IncomingRpcMethod$ParameterlessMethod<>(methodInfo, attributes, this.parameterlessFunction);
         } else if (this.parameterFunction != null) {
            if (this.paramInfo == null) {
               throw new IllegalStateException("No param schema defined");
            } else {
               return new IncomingRpcMethod$Method<>(methodInfo, attributes, this.parameterFunction);
            }
         } else {
            throw new IllegalStateException("No method defined");
         }
      }
   }

   public IncomingRpcMethod<?, ?> register(final Registry<IncomingRpcMethod<?, ?>> methodRegistry, final String key) {
      return this.register(methodRegistry, Identifier.withDefaultNamespace(key));
   }

   private IncomingRpcMethod<?, ?> register(final Registry<IncomingRpcMethod<?, ?>> methodRegistry, final Identifier id) {
      return Registry.register(methodRegistry, id, this.build());
   }
}
