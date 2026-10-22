package net.minecraft.client.data.models;

import com.google.gson.JsonElement;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput$PathProvider;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
class ModelProvider$SimpleModelCollector implements BiConsumer<Identifier, ModelInstance> {
   private final Map<Identifier, ModelInstance> models = new HashMap<>();

   private ModelProvider$SimpleModelCollector() {
   }

   public void accept(final Identifier id, final ModelInstance contents) {
      Supplier<JsonElement> prev = this.models.put(id, contents);
      if (prev != null) {
         throw new IllegalStateException("Duplicate model definition for " + id);
      }
   }

   public CompletableFuture<?> save(final CachedOutput cache, final PackOutput$PathProvider pathProvider) {
      return DataProvider.saveAll(cache, Supplier::get, pathProvider::json, this.models);
   }
}
