package com.mojang.realmsclient.gui.screens.configuration;

import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.Backup;
import com.mojang.realmsclient.exception.RealmsServiceException;
import java.util.List;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class RealmsBackupScreen$1 extends Thread {
   RealmsBackupScreen$1(final RealmsBackupScreen this$0, String name) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(name);
   }

   @Override
   public void run() {
      RealmsClient client = RealmsClient.getOrCreate();

      try {
         List<Backup> backups = client.backupsFor(this.this$0.serverData.id).backups();
         RealmsBackupScreen.access$000(this.this$0).execute(() -> {
            this.this$0.backups = backups;
            this.this$0.noBackups = this.this$0.backups.isEmpty();
            if (!this.this$0.noBackups && this.this$0.downloadButton != null) {
               this.this$0.downloadButton.active = true;
            }

            if (this.this$0.backupList != null) {
               this.this$0.backupList.replaceEntries(this.this$0.backups.stream().map(x$0 -> new RealmsBackupScreen$Entry(this.this$0, x$0)).toList());
            }
         });
      } catch (RealmsServiceException var3) {
         RealmsBackupScreen.LOGGER.error("Couldn't request backups", var3);
      }
   }
}
