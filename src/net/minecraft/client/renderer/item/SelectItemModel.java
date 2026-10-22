package net.minecraft.client.renderer.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class SelectItemModel<T> implements ItemModel {
   private final SelectItemModelProperty<T> property;
   private final SelectItemModel$ModelSelector<T> models;

   public SelectItemModel(final SelectItemModelProperty<T> property, final SelectItemModel$ModelSelector<T> models) {
      this.property = property;
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
      T value = this.property.get(item, level, owner == null ? null : owner.asLivingEntity(), seed, displayContext);
      this.models.get(value, level).update(output, item, resolver, displayContext, level, owner, seed);
   }
}
