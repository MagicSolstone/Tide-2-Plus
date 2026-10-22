package net.minecraft.client.resources.server;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
enum ServerPackManager$RemovalReason {
   DOWNLOAD_FAILED(PackLoadFeedback$FinalResult.DOWNLOAD_FAILED),
   ACTIVATION_FAILED(PackLoadFeedback$FinalResult.ACTIVATION_FAILED),
   DECLINED(PackLoadFeedback$FinalResult.DECLINED),
   DISCARDED(PackLoadFeedback$FinalResult.DISCARDED),
   SERVER_REMOVED(null),
   SERVER_REPLACED(null);

   @Nullable
   private final PackLoadFeedback$FinalResult serverResponse;

   private ServerPackManager$RemovalReason(@Nullable final PackLoadFeedback$FinalResult serverResponse) {
      this.serverResponse = serverResponse;
   }
}
