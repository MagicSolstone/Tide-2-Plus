package com.li64.tide.network.messages;

import com.li64.tide.Tide;
import com.li64.tide.client.ClientAngler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

/** The player's fishing XP (and whether levels are on, fair play, the baseline) for the journal's angler page. */
public record AnglerSyncMsg(long xp, boolean fair, boolean enabled, int baseline) implements TidePacketPayload {
    public static final Identifier ID = Tide.resource("angler_sync");
    @Override public Identifier id() { return ID; }

    public AnglerSyncMsg(FriendlyByteBuf buf) {
        this(buf.readVarLong(), buf.readBoolean(), buf.readBoolean(), buf.readVarInt());
    }

    public static void encode(AnglerSyncMsg message, FriendlyByteBuf buf) {
        buf.writeVarLong(message.xp);
        buf.writeBoolean(message.fair);
        buf.writeBoolean(message.enabled);
        buf.writeVarInt(message.baseline);
    }

    public static void handle(AnglerSyncMsg message, Player player) {
        ClientAngler.update(message.xp, message.fair, message.enabled, message.baseline);
    }
}
