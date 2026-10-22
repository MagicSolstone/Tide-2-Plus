package com.li64.tide.network.messages;

import com.li64.tide.Tide;
import com.li64.tide.data.minigame.FishCatchMinigame;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

/** event 0 = timed out, 5 = hooked (with accuracy); 1-3 are the old miss/catch/perfect. */
public record MinigameServerMsg(byte event, float accuracy) implements TidePacketPayload {
    public static final Identifier ID = Tide.resource("minigame_server");
    @Override public Identifier id() { return ID; }

    public MinigameServerMsg(byte event) {
        this(event, 0f);
    }

    public MinigameServerMsg(FriendlyByteBuf buf) {
        this(buf.readByte(), buf.readFloat());
    }

    public static void encode(MinigameServerMsg message, FriendlyByteBuf buf) {
        buf.writeByte(message.event);
        buf.writeFloat(message.accuracy);
    }

    public static void handle(MinigameServerMsg message, Player player) {
        FishCatchMinigame minigame = FishCatchMinigame.create(player);
        if (message.event == 5) minigame.handleHook(message.accuracy);
        else minigame.handleClientEvent(message.event);
    }
}