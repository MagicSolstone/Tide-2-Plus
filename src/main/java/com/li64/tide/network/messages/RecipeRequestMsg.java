package com.li64.tide.network.messages;

import com.li64.tide.Tide;
import com.li64.tide.data.tackle.TackleLog;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

/** The journal asks how a piece of tackle is made (only for tackle the player has found). */
public record RecipeRequestMsg(String item) implements TidePacketPayload {
    public static final Identifier ID = Tide.resource("recipe_request");
    @Override public Identifier id() { return ID; }

    public RecipeRequestMsg(FriendlyByteBuf buf) {
        this(buf.readUtf(128));
    }

    public static void encode(RecipeRequestMsg message, FriendlyByteBuf buf) {
        buf.writeUtf(message.item, 128);
    }

    public static void handle(RecipeRequestMsg message, Player player) {
        if (!(player instanceof ServerPlayer sp)) return;
        String path = message.item.startsWith("tide:") ? message.item.substring(5) : message.item;
        if (!TackleLog.found(sp).contains(path)) return;           // no peeking at undiscovered tackle
        Tide.NETWORK.sendToPlayer(TackleLog.recipeFor(sp.level().getServer(), message.item), sp);
    }
}
