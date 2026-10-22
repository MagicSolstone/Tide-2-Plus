package com.li64.tide.network.messages;

import com.li64.tide.Tide;
import com.li64.tide.client.gui.overlays.FishingInfoOverlay;
import com.li64.tide.registries.items.InformationalItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;

public record InfoDataMsg(Map<Identifier, String> data) implements TidePacketPayload {
    public static final Identifier ID = Tide.resource("survey_data");
    @Override public Identifier id() { return ID; }

    public InfoDataMsg(FriendlyByteBuf buf) {
        this(fromBuffer(buf));
    }

    private static Map<Identifier, String> fromBuffer(FriendlyByteBuf buf) {
        int count = buf.readInt();
        Map<Identifier, String> data = new HashMap<>(count);
        for (int i = 0; i < count; i++) {
            data.put(buf.readIdentifier(), buf.readUtf());
        }
        return data;
    }

    public static void encode(InfoDataMsg message, FriendlyByteBuf buf) {
        buf.writeInt(message.data.size());
        message.data.forEach((item, data) -> {
            buf.writeIdentifier(item);
            buf.writeUtf(data);
        });
    }

    public static void handle(InfoDataMsg message, Player player) {
        Map<Identifier, Component> results = new HashMap<>();
        message.data().forEach((key, data) -> {
            Item item = BuiltInRegistries.ITEM.getValue(key);
            results.put(key, ((InformationalItem)item).parseResult(data));
        });
        FishingInfoOverlay.CLIENT_INFO_DATA = results;
    }
}