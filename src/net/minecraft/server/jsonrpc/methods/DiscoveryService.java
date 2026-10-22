package net.minecraft.server.jsonrpc.methods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.jsonrpc.api.MethodInfo$Named;
import net.minecraft.server.jsonrpc.api.Schema;
import net.minecraft.server.jsonrpc.api.SchemaComponent;

public class DiscoveryService {
   public static DiscoveryService$DiscoverResponse discover(final List<SchemaComponent<?>> schemaRegistry) {
      List<MethodInfo$Named<?, ?>> methods = new ArrayList<>(BuiltInRegistries.INCOMING_RPC_METHOD.size() + BuiltInRegistries.OUTGOING_RPC_METHOD.size());
      BuiltInRegistries.INCOMING_RPC_METHOD.listElements().forEach(e -> {
         if (e.value().attributes().discoverable()) {
            methods.add(e.value().info().named(e.key().identifier()));
         }
      });
      BuiltInRegistries.OUTGOING_RPC_METHOD.listElements().forEach(e -> {
         if (e.value().attributes().discoverable()) {
            methods.add(e.value().info().named(e.key().identifier()));
         }
      });
      Map<String, Schema<?>> schemas = new HashMap<>();

      for (SchemaComponent<?> component : schemaRegistry) {
         schemas.put(component.name(), component.schema().info());
      }

      DiscoveryService$DiscoverInfo discoverInfo = new DiscoveryService$DiscoverInfo("Minecraft Server JSON-RPC", "3.1.0");
      return new DiscoveryService$DiscoverResponse("1.3.2", discoverInfo, methods, new DiscoveryService$DiscoverComponents(schemas));
   }
}
