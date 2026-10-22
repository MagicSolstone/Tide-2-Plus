package com.li64.tide.network.messages;

import com.li64.tide.Tide;
import com.li64.tide.client.TideClientHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

public class OpenJournalMsg implements TidePacketPayload {
    public static final Identifier ID = Tide.resource("open_journal");
    @Override public Identifier id() { return ID; }

    public OpenJournalMsg() {}
    public OpenJournalMsg(FriendlyByteBuf buf) {}

    public static void encode(OpenJournalMsg message, FriendlyByteBuf buf) {}

    public static void handle(OpenJournalMsg message, Player player) {
        TideClientHelper.openJournalScreen();
    }
}