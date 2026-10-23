package com.li64.tide.network.messages;

import com.li64.tide.Tide;
import com.li64.tide.registries.items.TackleBoxItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/** The tackle box screens' buttons: switch between the box and rigging a rod from it. */
public record OpenTackleMsg(boolean rig) implements TidePacketPayload {
    public static final Identifier ID = Tide.resource("open_tackle");
    @Override public Identifier id() { return ID; }

    public OpenTackleMsg(FriendlyByteBuf buf) {
        this(buf.readBoolean());
    }

    public static void encode(OpenTackleMsg message, FriendlyByteBuf buf) {
        buf.writeBoolean(message.rig);
    }

    public static void handle(OpenTackleMsg message, Player player) {
        if (!(player instanceof ServerPlayer sp)) return;
        ItemStack box = TackleBoxItem.held(sp);
        if (!box.isEmpty()) TackleBoxItem.open(sp, box, message.rig);
    }
}
