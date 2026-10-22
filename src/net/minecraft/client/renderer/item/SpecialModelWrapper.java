package net.minecraft.client.renderer.item;

import com.google.common.base.Suppliers;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4fc;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class SpecialModelWrapper<T> implements ItemModel {
   private final SpecialModelRenderer<T> specialRenderer;
   private final ModelRenderProperties properties;
   private final Supplier<Vector3fc[]> extents;
   private final Matrix4fc transformation;

   public SpecialModelWrapper(final SpecialModelRenderer<T> specialRenderer, final ModelRenderProperties properties, final Matrix4fc transformation) {
      this.specialRenderer = specialRenderer;
      this.properties = properties;
      this.extents = Suppliers.memoize(() -> {
         Set<Vector3fc> results = new HashSet<>();
         specialRenderer.getExtents(results::add);
         return results.toArray(new Vector3fc[0]);
      });
      this.transformation = transformation;
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
      ItemStackRenderState$LayerRenderState layer = output.newLayer();
      if (item.hasFoil()) {
         ItemStackRenderState$FoilType foilType = ItemStackRenderState$FoilType.STANDARD;
         layer.setFoilType(foilType);
         output.setAnimated();
         output.appendModelIdentityElement(foilType);
      }

      T argument = this.specialRenderer.extractArgument(item);
      layer.setExtents(this.extents);
      layer.setLocalTransform(this.transformation);
      layer.setupSpecialModel(this.specialRenderer, argument);
      if (argument != null) {
         output.appendModelIdentityElement(argument);
      }

      this.properties.applyToLayer(layer, displayContext);
   }
}
