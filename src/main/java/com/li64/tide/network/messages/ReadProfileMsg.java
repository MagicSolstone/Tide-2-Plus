package com.li64.tide.network.messages;

import com.li64.tide.Tide;
import com.li64.tide.data.player.TidePlayerData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record ReadProfileMsg(int fish) implements TidePacketPayload {
    public static final Identifier ID = Tide.resource("read_profile");
    @Override public Identifier id() { return ID; }

    public ReadProfileMsg(ItemStack fishItem) {
        this(Item.getId(fishItem.getItem()));
    }

    public ReadProfileMsg(FriendlyByteBuf buf) {
        this(buf.readInt());
    }

    public static void encode(ReadProfileMsg message, FriendlyByteBuf buf) {
        buf.writeInt(message.fish);
    }

    public static void handle(ReadProfileMsg message, Player player) {
        TidePlayerData data = TidePlayerData.getOrCreate((ServerPlayer) player);
        data.markAsRead(new ItemStack(Item.byId(message.fish)));
        data.syncTo((ServerPlayer) player);
    }
}