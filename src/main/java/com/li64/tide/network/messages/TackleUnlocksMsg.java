package com.li64.tide.network.messages;

import com.li64.tide.Tide;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/** The Angling Table recipes this player has discovered (the recipe book shows only these). */
public record TackleUnlocksMsg(List<String> ids) implements TidePacketPayload {
    public static final Identifier ID = Tide.resource("tackle_unlocks");
    @Override public Identifier id() { return ID; }

    public static volatile Set<String> CLIENT = Set.of();

    public TackleUnlocksMsg(FriendlyByteBuf buf) {
        this(read(buf));
    }

    private static List<String> read(FriendlyByteBuf buf) {
        int n = buf.readVarInt();
        List<String> out = new ArrayList<>(n);
        for (int i = 0; i < n; i++) out.add(buf.readUtf());
        return out;
    }

    public static void encode(TackleUnlocksMsg m, FriendlyByteBuf buf) {
        buf.writeVarInt(m.ids.size());
        for (String s : m.ids) buf.writeUtf(s);
    }

    public static void handle(TackleUnlocksMsg m, Player player) {
        CLIENT = Set.copyOf(m.ids);
    }
}
