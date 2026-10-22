package net.minecraft.world.level.storage;

import java.nio.file.Path;
import net.minecraft.network.chat.Component;

public class LevelSummary$CorruptedLevelSummary extends LevelSummary {
   private static final Component INFO = Component.translatable("recover_world.warning").withStyle(style -> style.withColor(-65536));
   private static final Component RECOVER = Component.translatable("recover_world.button");
   private final long lastPlayed;

   public LevelSummary$CorruptedLevelSummary(final String levelId, final Path icon, final long lastPlayed) {
      super(null, null, levelId, false, false, false, false, icon);
      this.lastPlayed = lastPlayed;
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
      return this.lastPlayed;
   }

   @Override
   public boolean isDisabled() {
      return false;
   }

   @Override
   public Component primaryActionMessage() {
      return RECOVER;
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
