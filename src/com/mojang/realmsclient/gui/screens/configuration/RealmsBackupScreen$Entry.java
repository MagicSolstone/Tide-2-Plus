package com.mojang.realmsclient.gui.screens.configuration;

import com.mojang.realmsclient.dto.Backup;
import com.mojang.realmsclient.gui.screens.RealmsLongRunningMcoTaskScreen;
import com.mojang.realmsclient.gui.screens.RealmsPopups;
import com.mojang.realmsclient.util.RealmsUtil;
import com.mojang.realmsclient.util.task.RestoreTask;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList$Entry;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class RealmsBackupScreen$Entry extends ContainerObjectSelectionList$Entry<RealmsBackupScreen$Entry> {
   private static final int Y_PADDING = 2;
   private final Backup backup;
   @Nullable
   private Button restoreButton;
   @Nullable
   private Button changesButton;
   private final List<AbstractWidget> children;

   public RealmsBackupScreen$Entry(final RealmsBackupScreen this$0, final Backup backup) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.children = new ArrayList<>();
      this.backup = backup;
      this.populateChangeList(backup);
      if (!backup.changeList.isEmpty()) {
         this.changesButton = Button.builder(
               RealmsBackupScreen.HAS_CHANGES_TOOLTIP,
               var2 -> RealmsBackupScreen.access$200(this$0x).gui.setScreen(new RealmsBackupInfoScreen(this$0x, this.backup))
            )
            .width(8 + RealmsBackupScreen.access$100(this$0x).width(RealmsBackupScreen.HAS_CHANGES_TOOLTIP))
            .createNarration(this::narrationForBackupEntry)
            .build();
         this.children.add(this.changesButton);
      }

      if (!this$0x.serverData.expired) {
         this.restoreButton = Button.builder(RealmsBackupScreen.RESTORE_TOOLTIP, button -> this.restoreClicked())
            .width(8 + RealmsBackupScreen.access$300(this$0x).width(RealmsBackupScreen.HAS_CHANGES_TOOLTIP))
            .createNarration(this::narrationForBackupEntry)
            .build();
         this.children.add(this.restoreButton);
      }
   }

   private MutableComponent narrationForBackupEntry(final Supplier<MutableComponent> defaultNarrationSupplier) {
      return CommonComponents.joinForNarration(
         Component.translatable("mco.backup.narration", RealmsBackupScreen.SHORT_DATE_FORMAT.format(this.backup.lastModifiedDate())),
         defaultNarrationSupplier.get()
      );
   }

   private void populateChangeList(final Backup backup) {
      int index = this.this$0.backups.indexOf(backup);
      if (index != this.this$0.backups.size() - 1) {
         Backup olderBackup = this.this$0.backups.get(index + 1);

         for (String key : backup.metadata.keySet()) {
            if (!key.contains("uploaded") && olderBackup.metadata.containsKey(key)) {
               if (!backup.metadata.get(key).equals(olderBackup.metadata.get(key))) {
                  this.addToChangeList(key);
               }
            } else {
               this.addToChangeList(key);
            }
         }
      }
   }

   private void addToChangeList(final String key) {
      if (key.contains("uploaded")) {
         String uploadedTime = RealmsBackupScreen.SHORT_DATE_FORMAT.format(this.backup.lastModifiedDate());
         this.backup.changeList.put(key, uploadedTime);
         this.backup.uploadedVersion = true;
      } else {
         this.backup.changeList.put(key, this.backup.metadata.get(key));
      }
   }

   private void restoreClicked() {
      Component age = RealmsUtil.convertToAgePresentationFromInstant(this.backup.lastModified);
      String lastModifiedDate = RealmsBackupScreen.SHORT_DATE_FORMAT.format(this.backup.lastModifiedDate());
      Component popupMessage = Component.translatable("mco.configure.world.restore.question.line1", lastModifiedDate, age);
      RealmsBackupScreen.access$500(this.this$0)
         .gui
         .setScreen(
            RealmsPopups.warningPopupScreen(
               this.this$0,
               popupMessage,
               var1x -> {
                  RealmsConfigureWorldScreen newScreen = this.this$0.lastScreen.getNewScreen();
                  RealmsBackupScreen.access$400(this.this$0)
                     .gui
                     .setScreen(new RealmsLongRunningMcoTaskScreen(newScreen, new RestoreTask(this.backup, this.this$0.serverData.id, newScreen)));
               }
            )
         );
   }

   @Override
   public List<? extends GuiEventListener> children() {
      return this.children;
   }

   @Override
   public List<? extends NarratableEntry> narratables() {
      return this.children;
   }

   @Override
   public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
      int middle = this.getContentYMiddle();
      int firstLineYPos = middle - 9 - 2;
      int secondLineYPos = middle + 2;
      int color = this.backup.uploadedVersion ? -8388737 : -1;
      graphics.text(
         RealmsBackupScreen.access$700(this.this$0),
         Component.translatable("mco.backup.entry", RealmsUtil.convertToAgePresentationFromInstant(this.backup.lastModified)),
         this.getContentX(),
         firstLineYPos,
         color
      );
      graphics.text(
         RealmsBackupScreen.access$800(this.this$0),
         RealmsBackupScreen.SHORT_DATE_FORMAT.format(this.backup.lastModifiedDate()),
         this.getContentX(),
         secondLineYPos,
         -11776948
      );
      int iconXOffet = 0;
      int iconYPos = this.getContentYMiddle() - 10;
      if (this.restoreButton != null) {
         iconXOffet += this.restoreButton.getWidth() + 8;
         this.restoreButton.setX(this.getContentRight() - iconXOffet);
         this.restoreButton.setY(iconYPos);
         this.restoreButton.extractRenderState(graphics, mouseX, mouseY, a);
      }

      if (this.changesButton != null) {
         iconXOffet += this.changesButton.getWidth() + 8;
         this.changesButton.setX(this.getContentRight() - iconXOffet);
         this.changesButton.setY(iconYPos);
         this.changesButton.extractRenderState(graphics, mouseX, mouseY, a);
      }
   }
}
