package net.minecraft.client.renderer.item;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class CompositeModel implements ItemModel {
   private final List<ItemModel> models;

   public CompositeModel(final List<ItemModel> models) {
      this.models = models;
   }

   @Override
   public void update(
      final ItemStackRenderState output,
      final ItemStack item,
      final ItemModelResolver resolver,
      final ItemDisplayContext displayContext,
      @Nullable final ClientLevel level,
      @Nullable final ItemOwner owner,
      final int seed
   ) {
      output.appendModelIdentityElement(this);
      output.ensureCapacity(this.models.size());

      for (ItemModel model : this.models) {
         model.update(output, item, resolver, displayContext, level, owner, seed);
      }
   }
}
