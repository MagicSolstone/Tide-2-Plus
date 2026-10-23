package com.li64.tide.network.messages;

import com.li64.tide.Tide;
import com.li64.tide.client.ClientTackle;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

/** The tackle the player has found, for the journal's tackle section. */
public record TackleSyncMsg(List<String> found) implements TidePacketPayload {
    public static final Identifier ID = Tide.resource("tackle_sync");
    @Override public Identifier id() { return ID; }

    public TackleSyncMsg(FriendlyByteBuf buf) {
        this(read(buf));
    }

    private static List<String> read(FriendlyByteBuf buf) {
        int n = buf.readVarInt();
        List<String> out = new ArrayList<>(n);
        for (int i = 0; i < n; i++) out.add(buf.readUtf());
        return out;
    }

    public static void encode(TackleSyncMsg message, FriendlyByteBuf buf) {
        buf.writeVarInt(message.found.size());
        for (String s : message.found) buf.writeUtf(s);
    }

    public static void handle(TackleSyncMsg message, Player player) {
        ClientTackle.found = new java.util.HashSet<>(message.found);
    }
}
