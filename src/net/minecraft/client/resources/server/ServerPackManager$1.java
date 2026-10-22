package net.minecraft.client.resources.server;

import java.util.List;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class ServerPackManager$1 implements PackReloadConfig$Callbacks {
   ServerPackManager$1(final ServerPackManager this$0, final List val$packsToLoad, final List val$packsToUnload) {
      this.val$packsToLoad = val$packsToLoadx;
      this.val$packsToUnload = val$packsToUnloadx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public void onSuccess() {
      for (ServerPackManager$ServerPackData pack : this.val$packsToLoad) {
         pack.activationStatus = ServerPackManager$ActivationStatus.ACTIVE;
         if (pack.removalReason == null) {
            this.this$0.packLoadFeedback.reportFinalResult(pack.id, PackLoadFeedback$FinalResult.APPLIED);
         }
      }

      for (ServerPackManager$ServerPackData packx : this.val$packsToUnload) {
         packx.activationStatus = ServerPackManager$ActivationStatus.INACTIVE;
      }

      this.this$0.registerForUpdate();
   }

   @Override
   public void onFailure(final boolean isRecovery) {
      if (!isRecovery) {
         this.val$packsToLoad.clear();

         for (ServerPackManager$ServerPackData pack : this.this$0.packs) {
            switch (pack.activationStatus) {
               case INACTIVE:
                  pack.setRemovalReasonIfNotSet(ServerPackManager$RemovalReason.DISCARDED);
                  break;
               case PENDING:
                  pack.activationStatus = ServerPackManager$ActivationStatus.INACTIVE;
                  pack.setRemovalReasonIfNotSet(ServerPackManager$RemovalReason.ACTIVATION_FAILED);
                  break;
               case ACTIVE:
                  this.val$packsToLoad.add(pack);
            }
         }

         this.this$0.registerForUpdate();
      } else {
         for (ServerPackManager$ServerPackData pack : this.this$0.packs) {
            if (pack.activationStatus == ServerPackManager$ActivationStatus.PENDING) {
               pack.activationStatus = ServerPackManager$ActivationStatus.INACTIVE;
            }
         }
      }
   }

   @Override
   public List<PackReloadConfig$IdAndPath> packsToLoad() {
      return this.val$packsToLoad.stream().map(pack -> new PackReloadConfig$IdAndPath(pack.id, pack.path)).toList();
   }
}
