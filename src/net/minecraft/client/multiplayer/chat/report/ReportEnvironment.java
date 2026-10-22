package net.minecraft.client.multiplayer.chat.report;

import com.mojang.authlib.services.request.AbuseReportRequest.ClientInfo;
import com.mojang.authlib.services.request.AbuseReportRequest.RealmInfo;
import com.mojang.authlib.services.request.AbuseReportRequest.ThirdPartyServerInfo;
import com.mojang.realmsclient.dto.RealmsServer;
import java.util.Locale;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record ReportEnvironment(String clientVersion, @Nullable ReportEnvironment$Server server) {
   public static ReportEnvironment local() {
      return create(null);
   }

   public static ReportEnvironment thirdParty(final String ip) {
      return create(new ReportEnvironment$Server$ThirdParty(ip));
   }

   public static ReportEnvironment realm(final RealmsServer realm) {
      return create(new ReportEnvironment$Server$Realm(realm));
   }

   public static ReportEnvironment create(@Nullable final ReportEnvironment$Server server) {
      return new ReportEnvironment(getClientVersion(), server);
   }

   public ClientInfo clientInfo() {
      return new ClientInfo(this.clientVersion, Locale.getDefault().toLanguageTag());
   }

   @Nullable
   public ThirdPartyServerInfo thirdPartyServerInfo() {
      return this.server instanceof ReportEnvironment$Server$ThirdParty thirdParty ? new ThirdPartyServerInfo(thirdParty.ip) : null;
   }

   @Nullable
   public RealmInfo realmInfo() {
      return this.server instanceof ReportEnvironment$Server$Realm realm ? new RealmInfo(String.valueOf(realm.realmId()), realm.slotId()) : null;
   }

   private static String getClientVersion() {
      StringBuilder version = new StringBuilder();
      version.append(SharedConstants.getCurrentVersion().id());
      if (Minecraft.checkModStatus().shouldReportAsModified()) {
         version.append(" (modded)");
      }

      return version.toString();
   }
}
