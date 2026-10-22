package net.minecraft.client.data.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.renderer.item.ClientItem;
import net.minecraft.client.renderer.item.ClientItem$Properties;
import net.minecraft.client.renderer.item.ItemModel$Unbaked;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput$PathProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

@Environment(EnvType.CLIENT)
class ModelProvider$ItemInfoCollector implements ItemModelOutput {
   private final Map<Item, ClientItem> itemInfos = new HashMap<>();
   private final Map<Item, Item> copies = new HashMap<>();

   private ModelProvider$ItemInfoCollector() {
   }

   @Override
   public void accept(final Item item, final ItemModel$Unbaked model, final ClientItem$Properties properties) {
      this.register(item, new ClientItem(model, properties));
   }

   private void register(final Item item, final ClientItem itemInfo) {
      ClientItem prev = this.itemInfos.put(item, itemInfo);
      if (prev != null) {
         throw new IllegalStateException("Duplicate item model definition for " + item);
      }
   }

   @Override
   public void copy(final Item donor, final Item acceptor) {
      this.copies.put(acceptor, donor);
   }

   public void finalizeAndValidate() {
      BuiltInRegistries.ITEM.forEach(item -> {
         if (!this.copies.containsKey(item)) {
            if (item instanceof BlockItem blockItem && !this.itemInfos.containsKey(blockItem)) {
               Identifier targetModel = ModelLocationUtils.getModelLocation(blockItem.getBlock());
               this.accept(blockItem, ItemModelUtils.plainModel(targetModel));
            }
         }
      });
      this.copies.forEach((acceptor, donor) -> {
         ClientItem donorInfo = this.itemInfos.get(donor);
         if (donorInfo == null) {
            throw new IllegalStateException("Missing donor: " + donor + " -> " + acceptor);
         } else {
            this.register(acceptor, donorInfo);
         }
      });
      List<Identifier> missingDefinitions = BuiltInRegistries.ITEM
         .listElements()
         .filter(e -> !this.itemInfos.containsKey(e.value()))
         .map(e -> e.key().identifier())
         .toList();
      if (!missingDefinitions.isEmpty()) {
         throw new IllegalStateException("Missing item model definitions for: " + missingDefinitions);
      }
   }

   public CompletableFuture<?> save(final CachedOutput cache, final PackOutput$PathProvider pathProvider) {
      return DataProvider.saveAll(cache, ClientItem.CODEC, item -> pathProvider.json(item.builtInRegistryHolder().key().identifier()), this.itemInfos);
   }
}
