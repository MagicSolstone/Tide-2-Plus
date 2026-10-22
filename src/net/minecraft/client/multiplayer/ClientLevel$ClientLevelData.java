package net.minecraft.client.multiplayer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.CrashReportCategory;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.storage.LevelData$RespawnData;
import net.minecraft.world.level.storage.WritableLevelData;

@Environment(EnvType.CLIENT)
public class ClientLevel$ClientLevelData implements WritableLevelData {
   private final boolean hardcore;
   private final boolean isFlat;
   private LevelData$RespawnData respawnData;
   private long gameTime;
   private Difficulty difficulty;
   private boolean difficultyLocked;

   public ClientLevel$ClientLevelData(final Difficulty difficulty, final boolean hardcore, final boolean isFlat) {
      this.difficulty = difficulty;
      this.hardcore = hardcore;
      this.isFlat = isFlat;
   }

   @Override
   public LevelData$RespawnData getRespawnData() {
      return this.respawnData;
   }

   @Override
   public long getGameTime() {
      return this.gameTime;
   }

   public void setGameTime(final long time) {
      this.gameTime = time;
   }

   @Override
   public void setSpawn(final LevelData$RespawnData respawnData) {
      this.respawnData = respawnData;
   }

   @Override
   public boolean isHardcore() {
      return this.hardcore;
   }

   @Override
   public Difficulty getDifficulty() {
      return this.difficulty;
   }

   @Override
   public boolean isDifficultyLocked() {
      return this.difficultyLocked;
   }

   @Override
   public void fillCrashReportCategory(final CrashReportCategory category, final LevelHeightAccessor levelHeightAccessor) {
      WritableLevelData.super.fillCrashReportCategory(category, levelHeightAccessor);
   }

   public void setDifficulty(final Difficulty difficulty) {
      this.difficulty = difficulty;
   }

   public void setDifficultyLocked(final boolean locked) {
      this.difficultyLocked = locked;
   }

   public double getHorizonHeight(final LevelHeightAccessor level) {
      return this.isFlat ? level.getMinY() : 63.0;
   }

   public float voidDarknessOnsetRange() {
      return this.isFlat ? 1.0F : 32.0F;
   }
}
