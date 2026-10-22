package net.minecraft.client.renderer.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ItemModelPropertyTest;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class ConditionalItemModel implements ItemModel {
   private final ItemModelPropertyTest property;
   private final ItemModel onTrue;
   private final ItemModel onFalse;

   public ConditionalItemModel(final ItemModelPropertyTest property, final ItemModel onTrue, final ItemModel onFalse) {
      this.property = property;
      this.onTrue = onTrue;
      this.onFalse = onFalse;
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
      (this.property.get(item, level, owner == null ? null : owner.asLivingEntity(), seed, displayContext) ? this.onTrue : this.onFalse)
         .update(output, item, resolver, displayContext, level, owner, seed);
   }
}
