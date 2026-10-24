package com.li64.tide.network;

import com.li64.tide.Tide;
import com.li64.tide.loaders.NetworkPlatform;
import com.li64.tide.network.messages.*;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import java.util.HashMap;
import java.util.Map;

public class TideMessages {
    public static final Map<Identifier, CustomPacketPayload.Type<? extends CustomPacketPayload>> ID_TO_TYPE = new HashMap<>();

    public static void init(NetworkPlatform network) {
        network.registerClientBoundPacket(OpenJournalMsg.class, OpenJournalMsg.ID, OpenJournalMsg::encode, OpenJournalMsg::new, OpenJournalMsg::handle);
        network.registerClientBoundPacket(ViewNoteMsg.class, ViewNoteMsg.ID, ViewNoteMsg::encode, ViewNoteMsg::new, ViewNoteMsg::handle);
        network.registerClientBoundPacket(MinigameClientMsg.class, MinigameClientMsg.ID, MinigameClientMsg::encode, MinigameClientMsg::new, MinigameClientMsg::handle);
        network.registerClientBoundPacket(EchoRodHookedMsg.class, EchoRodHookedMsg.ID, EchoRodHookedMsg::encode, EchoRodHookedMsg::new, EchoRodHookedMsg::handle);
        network.registerClientBoundPacket(InfoDataMsg.class, InfoDataMsg.ID, InfoDataMsg::encode, InfoDataMsg::new, InfoDataMsg::handle);
        network.registerClientBoundPacket(SyncPlayerDataMsg.class, SyncPlayerDataMsg.ID, SyncPlayerDataMsg::encode, SyncPlayerDataMsg::new, SyncPlayerDataMsg::handle);
        network.registerClientBoundPacket(ShowToastMsg.class, ShowToastMsg.ID, ShowToastMsg::encode, ShowToastMsg::new, ShowToastMsg::handle);
        network.registerClientBoundPacket(com.li64.tide.network.messages.TackleSyncMsg.class, com.li64.tide.network.messages.TackleSyncMsg.ID,
                com.li64.tide.network.messages.TackleSyncMsg::encode, com.li64.tide.network.messages.TackleSyncMsg::new, com.li64.tide.network.messages.TackleSyncMsg::handle);
        network.registerClientBoundPacket(com.li64.tide.network.messages.RecipeViewMsg.class, com.li64.tide.network.messages.RecipeViewMsg.ID,
                com.li64.tide.network.messages.RecipeViewMsg::encode, com.li64.tide.network.messages.RecipeViewMsg::new, com.li64.tide.network.messages.RecipeViewMsg::handle);
        network.registerClientBoundPacket(com.li64.tide.network.messages.CatchBannerMsg.class, com.li64.tide.network.messages.CatchBannerMsg.ID,
                com.li64.tide.network.messages.CatchBannerMsg::encode, com.li64.tide.network.messages.CatchBannerMsg::new, com.li64.tide.network.messages.CatchBannerMsg::handle);
        network.registerClientBoundPacket(com.li64.tide.network.messages.RecordsSyncMsg.class, com.li64.tide.network.messages.RecordsSyncMsg.ID,
                com.li64.tide.network.messages.RecordsSyncMsg::encode, com.li64.tide.network.messages.RecordsSyncMsg::new, com.li64.tide.network.messages.RecordsSyncMsg::handle);
        network.registerClientBoundPacket(TackleRecipesMsg.class, TackleRecipesMsg.ID, TackleRecipesMsg::encode, TackleRecipesMsg::new, TackleRecipesMsg::handle);
        network.registerClientBoundPacket(AnglerSyncMsg.class, AnglerSyncMsg.ID, AnglerSyncMsg::encode, AnglerSyncMsg::new, AnglerSyncMsg::handle);
        network.registerClientBoundPacket(SyncDataMsg.class, SyncDataMsg.ID, SyncDataMsg::encode, SyncDataMsg::new, SyncDataMsg::handle);

         /*if (Tide.PLATFORM.isModLoaded("starcatcher")) network.registerClientBoundPacket(StarcatcherStartMinigameMsg.class, StarcatcherStartMinigameMsg.ID, StarcatcherStartMinigameMsg::encode, StarcatcherStartMinigameMsg::new, StarcatcherStartMinigameMsg::handle);*/

        network.registerServerBoundPacket(MinigameServerMsg.class, MinigameServerMsg.ID, MinigameServerMsg::encode, MinigameServerMsg::new, MinigameServerMsg::handle);
        network.registerServerBoundPacket(com.li64.tide.network.messages.RecipeRequestMsg.class, com.li64.tide.network.messages.RecipeRequestMsg.ID,
                com.li64.tide.network.messages.RecipeRequestMsg::encode, com.li64.tide.network.messages.RecipeRequestMsg::new, com.li64.tide.network.messages.RecipeRequestMsg::handle);
        network.registerServerBoundPacket(com.li64.tide.network.messages.OpenTackleMsg.class, com.li64.tide.network.messages.OpenTackleMsg.ID,
                com.li64.tide.network.messages.OpenTackleMsg::encode, com.li64.tide.network.messages.OpenTackleMsg::new, com.li64.tide.network.messages.OpenTackleMsg::handle);
        network.registerServerBoundPacket(TackleCraftMsg.class, TackleCraftMsg.ID, TackleCraftMsg::encode, TackleCraftMsg::new, TackleCraftMsg::handle);
        network.registerServerBoundPacket(BenchTabMsg.class, BenchTabMsg.ID, BenchTabMsg::encode, BenchTabMsg::new, BenchTabMsg::handle);
        network.registerServerBoundPacket(ReadProfileMsg.class, ReadProfileMsg.ID, ReadProfileMsg::encode, ReadProfileMsg::new, ReadProfileMsg::handle);
    }
}
