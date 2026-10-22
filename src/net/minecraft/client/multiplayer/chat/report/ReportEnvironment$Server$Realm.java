package net.minecraft.client.multiplayer.chat.report;

import com.mojang.realmsclient.dto.RealmsServer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record ReportEnvironment$Server$Realm(long realmId, int slotId) implements ReportEnvironment$Server {
   public ReportEnvironment$Server$Realm(final RealmsServer realm) {
      this(realm.id, realm.activeSlot);
   }
}
