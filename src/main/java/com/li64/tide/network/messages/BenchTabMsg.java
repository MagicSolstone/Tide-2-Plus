package com.li64.tide.network.messages;

import com.li64.tide.Tide;
import com.li64.tide.client.gui.menus.AnglingCraftMenu;
import com.li64.tide.client.gui.menus.AnglingTableMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

/** The Angling Table's tabs: switch between Craft and Assemble at the same table. */
public record BenchTabMsg(boolean craft) implements TidePacketPayload {
    public static final Identifier ID = Tide.resource("bench_tab");
    @Override public Identifier id() { return ID; }

    public BenchTabMsg(FriendlyByteBuf buf) {
        this(buf.readBoolean());
    }

    public static void encode(BenchTabMsg m, FriendlyByteBuf buf) {
        buf.writeBoolean(m.craft);
    }

    public static void handle(BenchTabMsg m, Player player) {
        if (!(player instanceof ServerPlayer sp)) return;
        var menu = sp.containerMenu;
        if (!menu.stillValid(sp)) return;
        if (menu instanceof AnglingCraftMenu c && !m.craft) c.access().execute((level, pos) -> AnglingCraftMenu.open(sp, level, pos, false));
        else if (menu instanceof AnglingTableMenu t && t.getClass() == AnglingTableMenu.class && m.craft)
            t.access().execute((level, pos) -> AnglingCraftMenu.open(sp, level, pos, true));
    }
}
