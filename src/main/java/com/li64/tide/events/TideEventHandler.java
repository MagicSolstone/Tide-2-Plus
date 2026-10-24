package com.li64.tide.events;

import com.li64.tide.Tide;
import com.li64.tide.data.TideFishingManager;
import com.li64.tide.data.informational.FishingInfoManager;
import com.li64.tide.data.player.TidePlayerData;
import com.li64.tide.network.messages.SyncDataMsg;
import com.li64.tide.registries.TideItems;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class TideEventHandler {
    public static void onPlayerJoinWorld(ServerPlayer player) {
        com.li64.tide.data.angler.ServerRecords.fillFromJournal(player);
        Tide.NETWORK.sendToPlayer(new SyncDataMsg(), player);

        TidePlayerData playerData = TidePlayerData.getOrCreate(player);
        if (Tide.SERVER_CONFIG.journal.giveJournal && !playerData.gotJournal) {
            if (player.addItem(TideItems.FISHING_JOURNAL.getDefaultInstance()))
                playerData.gotJournal = true;
        }
        playerData.syncTo(player);
        com.li64.tide.data.angler.Angler.onJoin(player);
        com.li64.tide.data.tackle.TackleLog.sync(player);
    }

    public static void serverStarted() {
        Tide.FISHING_MANAGER = new TideFishingManager();
    }

    /** CI only: check every piece of fishing gear against every other once the server is up. */
    public static void serverStartedCi(MinecraftServer server) {
        if (Tide.CI && server.isDedicatedServer()) com.li64.tide.ci.GearCheck.run(server);
    }

    public static void endServerTick(MinecraftServer server) {
        server.getPlayerList().getPlayers().forEach(FishingInfoManager::tick);
        if (server.getTickCount() % 40 == 0) server.getPlayerList().getPlayers().forEach(com.li64.tide.data.tackle.TackleLog::tick);
    }
}
