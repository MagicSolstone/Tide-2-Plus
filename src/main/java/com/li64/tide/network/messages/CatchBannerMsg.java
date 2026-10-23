package com.li64.tide.network.messages;

import com.li64.tide.Tide;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * What you just caught, for the banner above the hotbar: the fish (icon), its name in its quality colour, its
 * size and anything special about it, and the XP it gave. Or a level-up (big = a new title).
 */
public record CatchBannerMsg(String item, String title, int color, String detail, String footer, boolean big, String weight) implements TidePacketPayload {
    public static final Identifier ID = Tide.resource("catch_banner");
    @Override public Identifier id() { return ID; }

    public CatchBannerMsg(FriendlyByteBuf buf) {
        this(buf.readUtf(), buf.readUtf(), buf.readInt(), buf.readUtf(), buf.readUtf(), buf.readBoolean(), buf.readUtf());
    }

    public static void encode(CatchBannerMsg m, FriendlyByteBuf buf) {
        buf.writeUtf(m.item); buf.writeUtf(m.title); buf.writeInt(m.color); buf.writeUtf(m.detail); buf.writeUtf(m.footer); buf.writeBoolean(m.big); buf.writeUtf(m.weight);
    }

    public static void handle(CatchBannerMsg m, Player player) {
        ItemStack icon = BuiltInRegistries.ITEM.getOptional(Identifier.tryParse(m.item)).map(ItemStack::new).orElse(ItemStack.EMPTY);
        com.li64.tide.client.gui.overlays.CatchBannerOverlay.show(icon, m.title, m.color, m.detail, m.footer, m.weight);
    }
}
