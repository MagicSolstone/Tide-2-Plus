package net.minecraft.world.level.block.entity;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

@FunctionalInterface
interface BellBlockEntity$ResonationEndAction {
   void run(final Level level, final BlockPos pos, List<LivingEntity> nearbyEntities);
}
