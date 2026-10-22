package net.minecraft.world.level.storage;

public enum LevelSummary$BackupStatus {
   NONE(false, false, ""),
   DOWNGRADE(true, true, "downgrade"),
   UPGRADE_TO_SNAPSHOT(true, false, "snapshot"),
   FILE_FIXING_REQUIRED(true, false, "file_fixing_required");

   private final boolean shouldBackup;
   private final boolean severe;
   private final String translationKey;

   private LevelSummary$BackupStatus(final boolean shouldBackup, final boolean severe, final String translationKey) {
      this.shouldBackup = shouldBackup;
      this.severe = severe;
      this.translationKey = translationKey;
   }

   public boolean shouldBackup() {
      return this.shouldBackup;
   }

   public boolean isSevere() {
      return this.severe;
   }

   public String getTranslationKey() {
      return this.translationKey;
   }
}
