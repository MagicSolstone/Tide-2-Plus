package net.minecraft.world.attribute;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public interface EnvironmentAttributeReader {
   EnvironmentAttributeReader EMPTY = new EnvironmentAttributeReader$1();

   <Value> Value getDimensionValue(EnvironmentAttribute<Value> attribute);

   default <Value> Value getValue(final EnvironmentAttribute<Value> attribute, final BlockPos pos) {
      return this.getValue(attribute, Vec3.atCenterOf(pos));
   }

   default <Value> Value getValue(final EnvironmentAttribute<Value> attribute, final Vec3 pos) {
      return this.getValue(attribute, pos, null);
   }

   <Value> Value getValue(EnvironmentAttribute<Value> attribute, Vec3 pos, @Nullable SpatialAttributeInterpolator biomeInterpolator);

   default <Value> Value getValue(final LootContext context, final EnvironmentAttribute<Value> attribute) {
      if (attribute.isPositional()) {
         Vec3 position = context.getOptional(LootContextParams.ORIGIN);
         return position != null ? this.getValue(attribute, position) : attribute.defaultValue();
      } else {
         return this.getDimensionValue(attribute);
      }
   }
}
