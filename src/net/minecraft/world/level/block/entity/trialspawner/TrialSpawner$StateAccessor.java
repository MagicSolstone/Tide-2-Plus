package net.minecraft.world.level.block.entity.trialspawner;

import net.minecraft.world.level.Level;

public interface TrialSpawner$StateAccessor {
   void setState(Level level, TrialSpawnerState state);

   TrialSpawnerState getState();

   void markUpdated();
}
