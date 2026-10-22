package net.minecraft.client.multiplayer;

import java.util.function.BooleanSupplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.ServerboundPacketListener;
import net.minecraft.network.protocol.Packet;

@Environment(EnvType.CLIENT)
record ClientCommonPacketListenerImpl$DeferredPacket(Packet<? extends ServerboundPacketListener> packet, BooleanSupplier sendCondition, long expirationTime) {
}
