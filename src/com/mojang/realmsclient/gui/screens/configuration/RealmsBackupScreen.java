package com.mojang.realmsclient.gui.screens.configuration;

import com.mojang.logging.LogUtils;
import com.mojang.realmsclient.dto.Backup;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.gui.screens.RealmsLongRunningMcoTaskScreen;
import com.mojang.realmsclient.gui.screens.RealmsPopups;
import com.mojang.realmsclient.util.task.DownloadTask;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.util.Util;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class RealmsBackupScreen extends RealmsScreen {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final Component TITLE = Component.translatable("mco.configure.world.backup");
   private static final Component RESTORE_TOOLTIP = Component.translatable("mco.backup.button.restore");
   private static final Component HAS_CHANGES_TOOLTIP = Component.translatable("mco.backup.changes.tooltip");
   private static final Component NO_BACKUPS_LABEL = Component.translatable("mco.backup.nobackups");
   private static final Component DOWNLOAD_LATEST = Component.translatable("mco.backup.button.download");
   private static final String UPLOADED_KEY = "uploaded";
   private static final int PADDING = 8;
   public static final DateTimeFormatter SHORT_DATE_FORMAT = Util.localizedDateFormatter(FormatStyle.SHORT);
   private final RealmsConfigureWorldScreen lastScreen;
   private List<Backup> backups = Collections.emptyList();
   private RealmsBackupScreen$BackupObjectSelectionList backupList;
   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
   private final int slotId;
   @Nullable
   private Button downloadButton;
   private final RealmsServer serverData;
   private boolean noBackups = false;

   public RealmsBackupScreen(final RealmsConfigureWorldScreen lastScreen, final RealmsServer serverData, final int slotId) {
      super(TITLE);
      this.lastScreen = lastScreen;
      this.serverData = serverData;
      this.slotId = slotId;
   }

   @Override
   public void init() {
      this.layout.addTitleHeader(TITLE, this.font);
      this.backupList = this.layout.addToContents(new RealmsBackupScreen$BackupObjectSelectionList(this));
      LinearLayout footer = this.layout.addToFooter(LinearLayout.horizontal().spacing(8));
      this.downloadButton = footer.addChild(Button.builder(DOWNLOAD_LATEST, button -> this.downloadClicked()).build());
      this.downloadButton.active = false;
      footer.addChild(Button.builder(CommonComponents.GUI_BACK, button -> this.onClose()).build());
      this.layout.visitWidgets(x$0 -> this.addRenderableWidget(x$0));
      this.repositionElements();
      this.fetchRealmsBackups();
   }

   @Override
   public void extractRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
      super.extractRenderState(graphics, mouseX, mouseY, a);
      if (this.noBackups && this.backupList != null) {
         graphics.text(
            this.font,
            NO_BACKUPS_LABEL,
            this.width / 2 - this.font.width(NO_BACKUPS_LABEL) / 2,
            this.backupList.getY() + this.backupList.getHeight() / 2 - 9 / 2,
            -1
         );
      }
   }

   @Override
   protected void repositionElements() {
      this.layout.arrangeElements();
      if (this.backupList != null) {
         this.backupList.updateSize(this.width, this.layout);
      }
   }

   private void fetchRealmsBackups() {
      new RealmsBackupScreen$1(this, "Realms-fetch-backups").start();
   }

   @Override
   public void onClose() {
      this.minecraft.gui.setScreen(this.lastScreen);
   }

   private void downloadClicked() {
      this.minecraft
         .gui
         .setScreen(
            RealmsPopups.infoPopupScreen(
               this,
               Component.translatable("mco.configure.world.restore.download.question.line1"),
               var1 -> this.minecraft
                  .gui
                  .setScreen(
                     new RealmsLongRunningMcoTaskScreen(
                        this.lastScreen.getNewScreen(),
                        new DownloadTask(
                           this.serverData.id,
                           this.slotId,
                           Objects.requireNonNullElse(this.serverData.name, "")
                              + " ("
                              + this.serverData.slots.get(this.serverData.activeSlot).options.getSlotName(this.serverData.activeSlot)
                              + ")",
                           this
                        )
                     )
                  )
            )
         );
   }
}
