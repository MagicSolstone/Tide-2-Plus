package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.gui.RealmsDataFetcher;
import com.mojang.realmsclient.gui.task.DataFetcher$Subscription;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
interface RealmsNotificationsScreen$DataFetcherConfiguration {
   DataFetcher$Subscription initDataFetcher(RealmsDataFetcher realmsDataFetcher);

   boolean showOldNotifications();
}
