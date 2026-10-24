package com.li64.tide.network.messages;

import com.li64.tide.Tide;
import com.li64.tide.client.gui.menus.AnglingCraftMenu;
import com.li64.tide.data.tackle.TackleRecipes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * The Angling Table's Craft button: make one (or as many as possible, up to a stack) of a recipe from what's in
 * the player's inventory. {@code prefer}: per ingredient, an item id to use first ("" for any).
 */
public record TackleCraftMsg(String recipe, List<String> prefer, boolean all) implements TidePacketPayload {
    public static final Identifier ID = Tide.resource("tackle_craft");
    @Override public Identifier id() { return ID; }

    public TackleCraftMsg(FriendlyByteBuf buf) {
        this(buf.readUtf(256), readList(buf), buf.readBoolean());
    }

    private static List<String> readList(FriendlyByteBuf buf) {
        int n = Math.min(16, buf.readVarInt());
        List<String> out = new ArrayList<>(n);
        for (int i = 0; i < n; i++) out.add(buf.readUtf(256));
        return out;
    }

    public static void encode(TackleCraftMsg m, FriendlyByteBuf buf) {
        buf.writeUtf(m.recipe, 256);
        buf.writeVarInt(m.prefer.size());
        for (String s : m.prefer) buf.writeUtf(s, 256);
        buf.writeBoolean(m.all);
    }

    public static void handle(TackleCraftMsg m, Player player) {
        if (!(player instanceof ServerPlayer sp) || !(sp.containerMenu instanceof AnglingCraftMenu menu) || !menu.stillValid(sp)) return;
        Identifier id = Identifier.tryParse(m.recipe);
        if (id == null) return;
        var entry = TackleRecipes.byId(id).orElse(null);
        if (entry == null) return;
        List<Item> prefer = new ArrayList<>();
        for (String s : m.prefer) {
            Identifier item = s.isEmpty() ? null : Identifier.tryParse(s);
            prefer.add(item == null ? null : BuiltInRegistries.ITEM.getOptional(item).orElse(null));
        }
        int made = 0, limit = m.all ? Math.max(1, entry.result().getDefaultMaxStackSize() / Math.max(1, entry.count())) : 1;
        while (made < limit && TackleRecipes.craftOnce(sp, entry, prefer)) made++;
        if (Tide.CI) Tide.LOG.info("TIDE-CI bench crafted {} x{}", m.recipe, made);
        if (made > 0) {
            sp.level().playSound(null, sp.blockPosition(), SoundEvents.VILLAGER_WORK_FISHERMAN, SoundSource.BLOCKS, 0.8f, 1.0f);
            menu.broadcastChanges();
        }
    }
}
