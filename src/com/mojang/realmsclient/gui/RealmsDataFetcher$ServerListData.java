package com.mojang.realmsclient.gui;

import com.mojang.realmsclient.dto.RealmsServer;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record RealmsDataFetcher$ServerListData(List<RealmsServer> serverList, List<RealmsServer> availableSnapshotServers) {
}
