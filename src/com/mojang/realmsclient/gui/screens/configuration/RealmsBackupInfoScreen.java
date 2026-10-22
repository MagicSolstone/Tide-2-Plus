package com.mojang.realmsclient.gui.screens.configuration;

import com.mojang.realmsclient.dto.Backup;
import com.mojang.realmsclient.dto.RealmsServer$WorldType;
import java.util.Locale;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.realms.RealmsScreen;

@Environment(EnvType.CLIENT)
public class RealmsBackupInfoScreen extends RealmsScreen {
   private static final Component TITLE = Component.translatable("mco.backup.info.title");
   private static final Component UNKNOWN = Component.translatable("mco.backup.unknown");
   private final Screen lastScreen;
   private final Backup backup;
   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
   private RealmsBackupInfoScreen$BackupInfoList backupInfoList;

   public RealmsBackupInfoScreen(final Screen lastScreen, final Backup backup) {
      super(TITLE);
      this.lastScreen = lastScreen;
      this.backup = backup;
   }

   @Override
   public void init() {
      this.layout.addTitleHeader(TITLE, this.font);
      this.backupInfoList = this.layout.addToContents(new RealmsBackupInfoScreen$BackupInfoList(this, this.minecraft));
      this.layout.addToFooter(Button.builder(CommonComponents.GUI_BACK, button -> this.onClose()).build());
      this.repositionElements();
      this.layout.visitWidgets(x$0 -> this.addRenderableWidget(x$0));
   }

   @Override
   protected void repositionElements() {
      this.backupInfoList.updateSize(this.width, this.layout);
      this.layout.arrangeElements();
   }

   @Override
   public void onClose() {
      this.minecraft.gui.setScreen(this.lastScreen);
   }

   private Component checkForSpecificMetadata(final String key, final String value) {
      String k = key.toLowerCase(Locale.ROOT);
      if (k.contains("game") && k.contains("mode")) {
         return this.gameModeMetadata(value);
      } else if (k.contains("game") && k.contains("difficulty")) {
         return this.gameDifficultyMetadata(value);
      } else {
         return (Component)(key.equals("world_type") ? this.parseWorldType(value) : Component.literal(value));
      }
   }

   private Component gameDifficultyMetadata(final String value) {
      try {
         return RealmsSlotOptionsScreen.DIFFICULTIES.get(Integer.parseInt(value)).getDisplayName();
      } catch (Exception var3) {
         return UNKNOWN;
      }
   }

   private Component gameModeMetadata(final String value) {
      try {
         return RealmsSlotOptionsScreen.GAME_MODES.get(Integer.parseInt(value)).getShortDisplayName();
      } catch (Exception var3) {
         return UNKNOWN;
      }
   }

   private Component parseWorldType(final String value) {
      try {
         return RealmsServer$WorldType.valueOf(value.toUpperCase(Locale.ROOT)).getDisplayName();
      } catch (Exception var3) {
         return RealmsServer$WorldType.UNKNOWN.getDisplayName();
      }
   }
}
