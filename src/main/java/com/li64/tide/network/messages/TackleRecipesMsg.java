package com.li64.tide.network.messages;

import com.li64.tide.Tide;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

/** What the Angling Table can make: sent on join and after a datapack reload. */
public record TackleRecipesMsg(List<Row> rows) implements TidePacketPayload {
    public static final Identifier ID = Tide.resource("tackle_recipes");
    @Override public Identifier id() { return ID; }

    /** {@code ingredients}: one entry per item used, each the item ids it accepts. */
    /** {@code grid}: where each ingredient goes in the 3x3 grid (0 = top left). */
    public record Row(String id, String result, int count, int kind, boolean smithing, List<List<String>> ingredients, List<Integer> grid) {}

    /** The client's copy. */
    public static volatile List<Row> CLIENT = List.of();

    public TackleRecipesMsg(FriendlyByteBuf buf) {
        this(read(buf));
    }

    private static List<Row> read(FriendlyByteBuf buf) {
        int n = buf.readVarInt();
        List<Row> out = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            String id = buf.readUtf(), result = buf.readUtf();
            int count = buf.readVarInt(), kind = buf.readVarInt();
            boolean smithing = buf.readBoolean();
            int m = buf.readVarInt();
            List<List<String>> ings = new ArrayList<>(m);
            for (int j = 0; j < m; j++) {
                int k = buf.readVarInt();
                List<String> opts = new ArrayList<>(k);
                for (int o = 0; o < k; o++) opts.add(buf.readUtf());
                ings.add(opts);
            }
            List<Integer> grid = new ArrayList<>(m);
            for (int j = 0; j < m; j++) grid.add(buf.readVarInt());
            out.add(new Row(id, result, count, kind, smithing, ings, grid));
        }
        return out;
    }

    public static void encode(TackleRecipesMsg m, FriendlyByteBuf buf) {
        buf.writeVarInt(m.rows.size());
        for (Row r : m.rows) {
            buf.writeUtf(r.id()); buf.writeUtf(r.result()); buf.writeVarInt(r.count()); buf.writeVarInt(r.kind()); buf.writeBoolean(r.smithing());
            buf.writeVarInt(r.ingredients().size());
            for (List<String> opts : r.ingredients()) {
                buf.writeVarInt(opts.size());
                for (String o : opts) buf.writeUtf(o);
            }
            for (int g : r.grid()) buf.writeVarInt(g);
        }
    }

    public static void handle(TackleRecipesMsg m, Player player) {
        CLIENT = List.copyOf(m.rows);
    }
}
