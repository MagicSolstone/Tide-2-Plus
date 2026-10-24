package com.li64.tide.network.messages;

import com.li64.tide.Tide;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;

/** The server's record fish (heaviest of each kind), for the journal: sent on join and whenever one is beaten. */
public record RecordsSyncMsg(Map<String, Entry> records) implements TidePacketPayload {
    public static final Identifier ID = Tide.resource("records_sync");
    @Override public Identifier id() { return ID; }

    public record Entry(String player, double grams, int quality) {}

    /** What the client knows (species id -> record). */
    public static volatile Map<String, Entry> CLIENT = Map.of();

    public RecordsSyncMsg(FriendlyByteBuf buf) {
        this(read(buf));
    }

    private static Map<String, Entry> read(FriendlyByteBuf buf) {
        int n = buf.readVarInt();
        Map<String, Entry> out = new HashMap<>(n);
        for (int i = 0; i < n; i++) out.put(buf.readUtf(), new Entry(buf.readUtf(), buf.readDouble(), buf.readInt()));
        return out;
    }

    public static void encode(RecordsSyncMsg m, FriendlyByteBuf buf) {
        buf.writeVarInt(m.records.size());
        m.records.forEach((k, e) -> {
            buf.writeUtf(k); buf.writeUtf(e.player()); buf.writeDouble(e.grams()); buf.writeInt(e.quality());
        });
    }

    public static void handle(RecordsSyncMsg m, Player player) {
        CLIENT = Map.copyOf(m.records);
    }
}
