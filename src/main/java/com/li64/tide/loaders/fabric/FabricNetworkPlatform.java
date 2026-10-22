package com.li64.tide.loaders.fabric;

import com.li64.tide.loaders.NetworkPlatform;
import com.li64.tide.network.TideMessages;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public class FabricNetworkPlatform implements NetworkPlatform {
    public ArrayList<ClientMsgRegistration<? extends CustomPacketPayload>> clientMessages = new ArrayList<>();

    @Override
    public <T extends CustomPacketPayload> void registerClientBoundPacket(Class<T> msgClass, ResourceLocation id, BiConsumer<T, RegistryFriendlyByteBuf> encoder, Function<RegistryFriendlyByteBuf, T> decoder, BiConsumer<T, Player> handler) {
        CustomPacketPayload.Type<T> type = this.idToType(id);
        PayloadTypeRegistry.playS2C().register(type, CustomPacketPayload.codec(encoder::accept, decoder::apply));
        clientMessages.add(new ClientMsgRegistration<>(type, handler));
    }

    @Override
    public <T extends CustomPacketPayload> void registerServerBoundPacket(Class<T> msgClass, ResourceLocation id, BiConsumer<T, RegistryFriendlyByteBuf> encoder, Function<RegistryFriendlyByteBuf, T> decoder, BiConsumer<T, Player> handler) {
        CustomPacketPayload.Type<T> type = this.idToType(id);
        PayloadTypeRegistry.playC2S().register(type, CustomPacketPayload.codec(encoder::accept, decoder::apply));
        ServerPlayNetworking.registerGlobalReceiver(type, (payload, context) ->
                context.player().server.execute(() -> handler.accept(payload, context.player())));
    }

    private <T extends CustomPacketPayload> CustomPacketPayload.Type<T> idToType(ResourceLocation id) {
        return (CustomPacketPayload.Type<T>) TideMessages.ID_TO_TYPE.computeIfAbsent(id, CustomPacketPayload.Type::new);
    }

    @Override
    public void sendToPlayer(CustomPacketPayload message, ServerPlayer player) {
        ServerPlayNetworking.send(player, message);
    }

    @Override
    public void sendToServer(CustomPacketPayload message) {
        ClientPlayNetworking.send(message);
    }

    @Override
    public void registerHandlers() {
        clientMessages.forEach(this::registerClientHandler);
    }

    public <T extends CustomPacketPayload> void registerClientHandler(ClientMsgRegistration<T> reg) {
        BiConsumer<T, Player> handler = reg.handler();
        ClientPlayNetworking.registerGlobalReceiver(reg.type(), (payload, context) ->
                context.client().execute(() -> handler.accept(payload, context.player())));
    }
}