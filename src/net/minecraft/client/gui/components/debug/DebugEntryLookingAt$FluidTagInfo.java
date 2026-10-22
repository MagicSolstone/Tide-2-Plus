package net.minecraft.client.gui.components.debug;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.HitResult;

@Environment(EnvType.CLIENT)
public class DebugEntryLookingAt$FluidTagInfo extends DebugEntryLookingAt$DebugEntryLookingAtTags<FluidState> {
   @Override
   public HitResult getHitResult(final Entity cameraEntity) {
      return cameraEntity.pick(20.0, 0.0F, true);
   }

   public FluidState getInstance(final Level level, final BlockPos pos) {
      return level.getFluidState(pos);
   }

   @Override
   public Identifier group() {
      return DebugEntryLookingAt.FLUID_GROUP;
   }
}
