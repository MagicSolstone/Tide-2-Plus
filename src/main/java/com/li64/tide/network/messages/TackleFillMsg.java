package com.li64.tide.network.messages;

import com.li64.tide.Tide;
import com.li64.tide.client.gui.menus.AnglingCraftMenu;
import com.li64.tide.data.tackle.TackleRecipes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

/** The table's recipe book: put a recipe's items into the grid from the inventory ({@code all}: as many sets as possible). */
public record TackleFillMsg(String recipe, boolean all) implements TidePacketPayload {
    public static final Identifier ID = Tide.resource("tackle_fill");
    @Override public Identifier id() { return ID; }

    public TackleFillMsg(FriendlyByteBuf buf) {
        this(buf.readUtf(256), buf.readBoolean());
    }

    public static void encode(TackleFillMsg m, FriendlyByteBuf buf) {
        buf.writeUtf(m.recipe, 256);
        buf.writeBoolean(m.all);
    }

    public static void handle(TackleFillMsg m, Player player) {
        if (!(player instanceof ServerPlayer sp) || !(sp.containerMenu instanceof AnglingCraftMenu menu) || !menu.stillValid(sp)) return;
        Identifier id = Identifier.tryParse(m.recipe);
        var entry = id == null ? null : TackleRecipes.byId(id).orElse(null);
        if (entry == null) return;
        boolean ok = TackleRecipes.fill(sp, entry, menu.grid(), m.all);
        menu.slotsChanged(menu.grid());
        menu.broadcastChanges();
        if (Tide.CI) Tide.LOG.info("TIDE-CI table filled {} ({})", m.recipe, ok ? "ok" : "short");
    }
}
