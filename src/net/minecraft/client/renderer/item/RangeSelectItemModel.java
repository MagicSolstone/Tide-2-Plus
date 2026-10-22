package net.minecraft.client.renderer.item;

import java.util.Arrays;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class RangeSelectItemModel implements ItemModel {
   private static final int LINEAR_SEARCH_THRESHOLD = 16;
   private final RangeSelectItemModelProperty property;
   private final float scale;
   private final float[] thresholds;
   private final ItemModel[] models;
   private final ItemModel fallback;

   private RangeSelectItemModel(
      final RangeSelectItemModelProperty property, final float scale, final float[] thresholds, final ItemModel[] models, final ItemModel fallback
   ) {
      this.property = property;
      this.thresholds = thresholds;
      this.models = models;
      this.fallback = fallback;
      this.scale = scale;
   }

   private static int lastIndexLessOrEqual(final float[] haystack, final float needle) {
      if (haystack.length < 16) {
         for (int i = 0; i < haystack.length; i++) {
            if (haystack[i] > needle) {
               return i - 1;
            }
         }

         return haystack.length - 1;
      } else {
         int index = Arrays.binarySearch(haystack, needle);
         if (index < 0) {
            int insertionPoint = ~index;
            return insertionPoint - 1;
         } else {
            return index;
         }
      }
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
      float value = this.property.get(item, level, owner, seed) * this.scale;
      ItemModel selectedModel;
      if (Float.isNaN(value)) {
         selectedModel = this.fallback;
      } else {
         int index = lastIndexLessOrEqual(this.thresholds, value);
         selectedModel = index == -1 ? this.fallback : this.models[index];
      }

      selectedModel.update(output, item, resolver, displayContext, level, owner, seed);
   }
}
