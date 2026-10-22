package net.minecraft.client.renderer.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.RegistryContextSwapper;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record ClientItem(ItemModel$Unbaked model, ClientItem$Properties properties, @Nullable RegistryContextSwapper registrySwapper) {
   public static final Codec<ClientItem> CODEC = RecordCodecBuilder.create(
      i -> i.group(ItemModels.CODEC.fieldOf("model").forGetter(ClientItem::model), ClientItem$Properties.MAP_CODEC.forGetter(ClientItem::properties))
         .apply(i, ClientItem::new)
   );

   public ClientItem(final ItemModel$Unbaked model, final ClientItem$Properties properties) {
      this(model, properties, null);
   }

   public ClientItem withRegistrySwapper(final RegistryContextSwapper registrySwapper) {
      return new ClientItem(this.model, this.properties, registrySwapper);
   }
}
