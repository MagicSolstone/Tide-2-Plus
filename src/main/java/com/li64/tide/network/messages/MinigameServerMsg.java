package com.li64.tide.network.messages;

import com.li64.tide.Tide;
import com.li64.tide.data.minigame.FishCatchMinigame;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

public record MinigameServerMsg(byte event) implements TidePacketPayload {
    public static final Identifier ID = Tide.resource("minigame_server");
    @Override public Identifier id() { return ID; }

    public MinigameServerMsg(FriendlyByteBuf buf) {
        this(buf.readByte());
    }

    public static void encode(MinigameServerMsg message, FriendlyByteBuf buf) {
        buf.writeByte(message.event);
    }

    public static void handle(MinigameServerMsg message, Player player) {
        FishCatchMinigame minigame = FishCatchMinigame.create(player);
        minigame.handleClientEvent(message.event);
    }
}