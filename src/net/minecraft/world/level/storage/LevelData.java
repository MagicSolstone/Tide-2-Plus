package net.minecraft.world.level.storage;

import net.minecraft.CrashReportCategory;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.LevelHeightAccessor;

public interface LevelData {
   LevelData$RespawnData getRespawnData();

   long getGameTime();

   boolean isHardcore();

   Difficulty getDifficulty();

   boolean isDifficultyLocked();

   default void fillCrashReportCategory(final CrashReportCategory category, final LevelHeightAccessor levelHeightAccessor) {
      category.setDetail("Level spawn location", () -> CrashReportCategory.formatLocation(levelHeightAccessor, this.getRespawnData().pos()));
   }
}
