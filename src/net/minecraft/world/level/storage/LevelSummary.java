package net.minecraft.world.level.storage;

import java.nio.file.Path;
import net.minecraft.ChatFormatting;
import net.minecraft.SharedConstants;
import net.minecraft.WorldVersion;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.StringUtil;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.LevelSettings;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.Nullable;

public class LevelSummary implements Comparable<LevelSummary> {
   public static final Component PLAY_WORLD = Component.translatable("selectWorld.select");
   public static final Component UPGRADE_AND_PLAY_WORLD = Component.translatable("selectWorld.upgrade_and_play");
   private final LevelSettings settings;
   private final LevelVersion levelVersion;
   private final String levelId;
   private final boolean requiresManualConversion;
   private final boolean requiresFileFixing;
   private final boolean locked;
   private final boolean experimental;
   private final Path icon;
   @Nullable
   private Component info;

   public LevelSummary(
      final LevelSettings settings,
      final LevelVersion levelVersion,
      final String levelId,
      final boolean requiresManualConversion,
      final boolean requiresFileFixing,
      final boolean locked,
      final boolean experimental,
      final Path icon
   ) {
      this.settings = settings;
      this.levelVersion = levelVersion;
      this.levelId = levelId;
      this.requiresFileFixing = requiresFileFixing;
      this.locked = locked;
      this.experimental = experimental;
      this.icon = icon;
      this.requiresManualConversion = requiresManualConversion;
   }

   public String getLevelId() {
      return this.levelId;
   }

   public String getLevelName() {
      return StringUtils.isEmpty(this.settings.levelName()) ? this.levelId : this.settings.levelName();
   }

   public Path getIcon() {
      return this.icon;
   }

   public boolean requiresManualConversion() {
      return this.requiresManualConversion;
   }

   public boolean requiresFileFixing() {
      return this.requiresFileFixing;
   }

   public boolean isExperimental() {
      return this.experimental;
   }

   public long getLastPlayed() {
      return this.levelVersion.lastPlayed();
   }

   public int compareTo(final LevelSummary rhs) {
      if (this.getLastPlayed() < rhs.getLastPlayed()) {
         return 1;
      } else {
         return this.getLastPlayed() > rhs.getLastPlayed() ? -1 : this.levelId.compareTo(rhs.levelId);
      }
   }

   public LevelSettings getSettings() {
      return this.settings;
   }

   public GameType getGameMode() {
      return this.settings.gameType();
   }

   public boolean isHardcore() {
      return this.settings.difficultySettings().hardcore();
   }

   public boolean hasCommands() {
      return this.settings.allowCommands();
   }

   public MutableComponent getWorldVersionName() {
      return StringUtil.isNullOrEmpty(this.levelVersion.minecraftVersionName())
         ? Component.translatable("selectWorld.versionUnknown")
         : Component.literal(this.levelVersion.minecraftVersionName());
   }

   public LevelVersion levelVersion() {
      return this.levelVersion;
   }

   public boolean shouldBackup() {
      return this.backupStatus().shouldBackup();
   }

   public boolean isDowngrade() {
      return this.backupStatus() == LevelSummary$BackupStatus.DOWNGRADE;
   }

   public LevelSummary$BackupStatus backupStatus() {
      WorldVersion currentVersion = SharedConstants.getCurrentVersion();
      int currentVersionNumber = currentVersion.dataVersion().version();
      int levelVersionNumber = this.levelVersion.minecraftVersion().version();
      if (DataFixers.getFileFixer().requiresFileFixing(levelVersionNumber)) {
         return LevelSummary$BackupStatus.FILE_FIXING_REQUIRED;
      } else if (!currentVersion.stable() && levelVersionNumber < currentVersionNumber) {
         return LevelSummary$BackupStatus.UPGRADE_TO_SNAPSHOT;
      } else {
         return levelVersionNumber > currentVersionNumber ? LevelSummary$BackupStatus.DOWNGRADE : LevelSummary$BackupStatus.NONE;
      }
   }

   public boolean isLocked() {
      return this.locked;
   }

   public boolean isDisabled() {
      return !this.isLocked() && !this.requiresManualConversion() ? !this.isCompatible() : true;
   }

   public boolean isCompatible() {
      return SharedConstants.getCurrentVersion().dataVersion().isCompatible(this.levelVersion.minecraftVersion());
   }

   public Component getInfo() {
      if (this.info == null) {
         this.info = this.createInfo();
      }

      return this.info;
   }

   private Component createInfo() {
      if (this.isLocked()) {
         return Component.translatable("selectWorld.locked").withStyle(ChatFormatting.RED);
      } else if (this.requiresManualConversion()) {
         return Component.translatable("selectWorld.conversion").withStyle(ChatFormatting.RED);
      } else if (!this.isCompatible()) {
         return Component.translatable("selectWorld.incompatible.info", this.getWorldVersionName()).withStyle(ChatFormatting.RED);
      } else {
         MutableComponent result = this.isHardcore()
            ? Component.empty().append(Component.translatable("gameMode.hardcore").withColor(-65536))
            : Component.translatable("gameMode." + this.getGameMode().getName());
         if (this.hasCommands()) {
            result.append(", ").append(Component.translatable("selectWorld.commands"));
         }

         if (this.isExperimental()) {
            result.append(", ").append(Component.translatable("selectWorld.experimental").withStyle(ChatFormatting.YELLOW));
         }

         MutableComponent worldVersionName = this.getWorldVersionName();
         MutableComponent decoratedVersionName = Component.literal(", ").append(Component.translatable("selectWorld.version")).append(CommonComponents.SPACE);
         if (this.shouldBackup()) {
            decoratedVersionName.append(worldVersionName.withStyle(this.isDowngrade() ? ChatFormatting.RED : ChatFormatting.ITALIC));
         } else {
            decoratedVersionName.append(worldVersionName);
         }

         result.append(decoratedVersionName);
         return result;
      }
   }

   public Component primaryActionMessage() {
      return this.requiresFileFixing() ? UPGRADE_AND_PLAY_WORLD : PLAY_WORLD;
   }

   public boolean primaryActionActive() {
      return !this.isDisabled();
   }

   public boolean canUpload() {
      return !this.requiresManualConversion() && !this.isLocked();
   }

   public boolean canEdit() {
      return !this.isDisabled() && !this.requiresFileFixing();
   }

   public boolean canRecreate() {
      return !this.isDisabled() && !this.requiresFileFixing();
   }

   public boolean canDelete() {
      return true;
   }
}
