package net.minecraft.client.resources.model;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel$UnbakedRoot;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

@Environment(EnvType.CLIENT)
record ModelGroupCollector$GroupKey(Object equalityGroup, List<Object> coloringValues) {
   public static ModelGroupCollector$GroupKey create(
      final BlockState state, final BlockStateModel$UnbakedRoot model, final List<Property<?>> coloringProperties
   ) {
      List<Object> coloringValues = getColoringValues(state, coloringProperties);
      Object equalityGroup = model.visualEqualityGroup(state);
      return new ModelGroupCollector$GroupKey(equalityGroup, coloringValues);
   }

   private static List<Object> getColoringValues(final BlockState state, final List<Property<?>> coloringProperties) {
      Object[] coloringValues = new Object[coloringProperties.size()];

      for (int i = 0; i < coloringProperties.size(); i++) {
         coloringValues[i] = state.getValue(coloringProperties.get(i));
      }

      return List.of(coloringValues);
   }
}
