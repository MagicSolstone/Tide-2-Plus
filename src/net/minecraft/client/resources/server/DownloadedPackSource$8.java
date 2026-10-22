package net.minecraft.client.resources.server;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class DownloadedPackSource$8 {
   static {
      try {
         $SwitchMap$net$minecraft$client$resources$server$ServerPackManager$PackPromptStatus[ServerPackManager$PackPromptStatus.ALLOWED.ordinal()] = 1;
      } catch (NoSuchFieldError var10) {
      }

      try {
         $SwitchMap$net$minecraft$client$resources$server$ServerPackManager$PackPromptStatus[ServerPackManager$PackPromptStatus.DECLINED.ordinal()] = 2;
      } catch (NoSuchFieldError var9) {
      }

      try {
         $SwitchMap$net$minecraft$client$resources$server$ServerPackManager$PackPromptStatus[ServerPackManager$PackPromptStatus.PENDING.ordinal()] = 3;
      } catch (NoSuchFieldError var8) {
      }

      $SwitchMap$net$minecraft$client$resources$server$PackLoadFeedback$FinalResult = new int[PackLoadFeedback$FinalResult.values().length];

      try {
         $SwitchMap$net$minecraft$client$resources$server$PackLoadFeedback$FinalResult[PackLoadFeedback$FinalResult.APPLIED.ordinal()] = 1;
      } catch (NoSuchFieldError var7) {
      }

      try {
         $SwitchMap$net$minecraft$client$resources$server$PackLoadFeedback$FinalResult[PackLoadFeedback$FinalResult.DOWNLOAD_FAILED.ordinal()] = 2;
      } catch (NoSuchFieldError var6) {
      }

      try {
         $SwitchMap$net$minecraft$client$resources$server$PackLoadFeedback$FinalResult[PackLoadFeedback$FinalResult.DECLINED.ordinal()] = 3;
      } catch (NoSuchFieldError var5) {
      }

      try {
         $SwitchMap$net$minecraft$client$resources$server$PackLoadFeedback$FinalResult[PackLoadFeedback$FinalResult.DISCARDED.ordinal()] = 4;
      } catch (NoSuchFieldError var4) {
      }

      try {
         $SwitchMap$net$minecraft$client$resources$server$PackLoadFeedback$FinalResult[PackLoadFeedback$FinalResult.ACTIVATION_FAILED.ordinal()] = 5;
      } catch (NoSuchFieldError var3) {
      }

      $SwitchMap$net$minecraft$client$resources$server$PackLoadFeedback$Update = new int[PackLoadFeedback$Update.values().length];

      try {
         $SwitchMap$net$minecraft$client$resources$server$PackLoadFeedback$Update[PackLoadFeedback$Update.ACCEPTED.ordinal()] = 1;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$client$resources$server$PackLoadFeedback$Update[PackLoadFeedback$Update.DOWNLOADED.ordinal()] = 2;
      } catch (NoSuchFieldError var1) {
      }
   }
}
