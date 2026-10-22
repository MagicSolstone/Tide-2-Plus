package net.minecraft.world.level.storage;

import java.nio.file.Path;
import net.minecraft.network.chat.Component;

public class LevelSummary$SymlinkLevelSummary extends LevelSummary {
   private static final Component MORE_INFO_BUTTON = Component.translatable("symlink_warning.more_info");
   private static final Component INFO = Component.translatable("symlink_warning.title").withColor(-65536);

   public LevelSummary$SymlinkLevelSummary(final String levelId, final Path icon) {
      super(null, null, levelId, false, false, false, false, icon);
   }

   @Override
   public String getLevelName() {
      return this.getLevelId();
   }

   @Override
   public Component getInfo() {
      return INFO;
   }

   @Override
   public long getLastPlayed() {
      return -1L;
   }

   @Override
   public boolean isDisabled() {
      return false;
   }

   @Override
   public Component primaryActionMessage() {
      return MORE_INFO_BUTTON;
   }

   @Override
   public boolean primaryActionActive() {
      return true;
   }

   @Override
   public boolean canUpload() {
      return false;
   }

   @Override
   public boolean canEdit() {
      return false;
   }

   @Override
   public boolean canRecreate() {
      return false;
   }
}
