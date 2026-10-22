package net.minecraft.client.data.models;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.item.ClientItem$Properties;
import net.minecraft.client.renderer.item.ItemModel$Unbaked;
import net.minecraft.world.item.Item;

@Environment(EnvType.CLIENT)
public interface ItemModelOutput {
   default void accept(final Item item, final ItemModel$Unbaked generator) {
      this.accept(item, generator, ClientItem$Properties.DEFAULT);
   }

   void accept(Item item, ItemModel$Unbaked generator, ClientItem$Properties properties);

   void copy(Item donor, Item acceptor);
}
