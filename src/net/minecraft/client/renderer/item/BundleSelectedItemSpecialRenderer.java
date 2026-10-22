package net.minecraft.client.renderer.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class BundleSelectedItemSpecialRenderer implements ItemModel {
   private static final ItemModel INSTANCE = new BundleSelectedItemSpecialRenderer();

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
      ItemStackTemplate selectedItem = BundleItem.getSelectedItem(item);
      if (selectedItem != null) {
         resolver.appendItemLayers(output, selectedItem.create(), displayContext, level, owner, seed);
      }
   }
}
