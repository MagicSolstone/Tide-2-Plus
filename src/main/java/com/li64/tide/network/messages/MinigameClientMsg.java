package com.li64.tide.network.messages;

import com.li64.tide.Tide;
import com.li64.tide.client.gui.overlays.CatchMinigameOverlay;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

public record MinigameClientMsg(byte event, byte display, byte behavior, float area, float speed) implements TidePacketPayload {
    public static final Identifier ID = Tide.resource("minigame_client");
    @Override public Identifier id() { return ID; }

    public MinigameClientMsg(int event) {
        this((byte) event, (byte) 0, (byte) 0, 0, 0);
    }

    public MinigameClientMsg(FriendlyByteBuf buf) {
        this(buf.readByte(), buf.readByte(), buf.readByte(), buf.readFloat(), buf.readFloat());
    }

    public static void encode(MinigameClientMsg message, FriendlyByteBuf buf) {
        buf.writeByte(message.event);
        buf.writeByte(message.display);
        buf.writeByte(message.behavior);
        buf.writeFloat(message.area);
        buf.writeFloat(message.speed);
    }

    public static void handle(MinigameClientMsg message, Player player) {
        switch (message.event) {
            case 0 -> CatchMinigameOverlay.start(message.display, message.behavior, message.area, message.speed);
            case 1 -> CatchMinigameOverlay.interact();
            case 2 -> CatchMinigameOverlay.close();
        }
    }
}