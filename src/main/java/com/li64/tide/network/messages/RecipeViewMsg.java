package com.li64.tide.network.messages;

import com.li64.tide.Tide;
import com.li64.tide.client.ClientTackle;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * How a piece of tackle is made, for the journal: a crafting grid (w x h slots, each a few item ids to cycle
 * through for tags like "any planks"), a smithing table (template, base, addition), or where it's found.
 */
public record RecipeViewMsg(String item, String kind, int width, int height, List<List<String>> slots, String note) implements TidePacketPayload {
    public static final Identifier ID = Tide.resource("recipe_view");
    @Override public Identifier id() { return ID; }

    public RecipeViewMsg(FriendlyByteBuf buf) {
        this(buf.readUtf(), buf.readUtf(), buf.readVarInt(), buf.readVarInt(), readSlots(buf), buf.readUtf());
    }

    private static List<List<String>> readSlots(FriendlyByteBuf buf) {
        int n = buf.readVarInt();
        List<List<String>> out = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            int m = buf.readVarInt();
            List<String> slot = new ArrayList<>(m);
            for (int j = 0; j < m; j++) slot.add(buf.readUtf());
            out.add(slot);
        }
        return out;
    }

    public static void encode(RecipeViewMsg m, FriendlyByteBuf buf) {
        buf.writeUtf(m.item);
        buf.writeUtf(m.kind);
        buf.writeVarInt(m.width);
        buf.writeVarInt(m.height);
        buf.writeVarInt(m.slots.size());
        for (List<String> slot : m.slots) {
            buf.writeVarInt(slot.size());
            for (String s : slot) buf.writeUtf(s);
        }
        buf.writeUtf(m.note);
    }

    public static void handle(RecipeViewMsg m, Player player) {
        ClientTackle.recipes.put(m.item, m);
    }
}
