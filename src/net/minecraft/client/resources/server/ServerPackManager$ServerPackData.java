package net.minecraft.client.resources.server;

import com.google.common.hash.HashCode;
import java.net.URL;
import java.nio.file.Path;
import java.util.UUID;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class ServerPackManager$ServerPackData {
   private final UUID id;
   private final URL url;
   @Nullable
   private final HashCode hash;
   @Nullable
   private Path path;
   @Nullable
   private ServerPackManager$RemovalReason removalReason;
   private ServerPackManager$PackDownloadStatus downloadStatus = ServerPackManager$PackDownloadStatus.REQUESTED;
   private ServerPackManager$ActivationStatus activationStatus = ServerPackManager$ActivationStatus.INACTIVE;
   private boolean promptAccepted;

   private ServerPackManager$ServerPackData(final UUID id, final URL url, @Nullable final HashCode hash) {
      this.id = id;
      this.url = url;
      this.hash = hash;
   }

   public void setRemovalReasonIfNotSet(final ServerPackManager$RemovalReason removalReason) {
      if (this.removalReason == null) {
         this.removalReason = removalReason;
      }
   }

   public boolean isRemoved() {
      return this.removalReason != null;
   }
}
