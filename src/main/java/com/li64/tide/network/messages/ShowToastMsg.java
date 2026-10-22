package com.li64.tide.network.messages;

import com.li64.tide.Tide;
import com.li64.tide.client.TideClientHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public record ShowToastMsg(Component title, Component description, ItemStack display) implements TidePacketPayload {
    public static final Identifier ID = Tide.resource("show_toast");
    @Override public Identifier id() { return ID; }

    public ShowToastMsg(FriendlyByteBuf buf) {
        this(Component.translatable(buf.readUtf()), Component.translatable(buf.readUtf()), BuiltInRegistries.ITEM
                .getOptional(Identifier.tryParse(buf.readUtf())).orElseThrow().getDefaultInstance());
    }

    public static void encode(ShowToastMsg message, FriendlyByteBuf buf) {
        buf.writeUtf(message.title.getString());
        buf.writeUtf(message.description.getString());
        buf.writeUtf(BuiltInRegistries.ITEM.getKey(message.display.getItem()).toString());
    }

    public static void handle(ShowToastMsg message, Player player) {
        TideClientHelper.showToast(message.title, message.description, message.display);
    }
}