package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.gui.RealmsDataFetcher;
import com.mojang.realmsclient.gui.task.DataFetcher$Subscription;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class RealmsNotificationsScreen$1 implements RealmsNotificationsScreen$DataFetcherConfiguration {
   RealmsNotificationsScreen$1(final RealmsNotificationsScreen this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public DataFetcher$Subscription initDataFetcher(final RealmsDataFetcher dataSource) {
      DataFetcher$Subscription result = dataSource.dataFetcher.createSubscription();
      this.this$0.addNewsAndInvitesSubscriptions(dataSource, result);
      this.this$0.addNotificationsSubscriptions(dataSource, result);
      return result;
   }

   @Override
   public boolean showOldNotifications() {
      return true;
   }
}
