package net.minecraft.network.protocol.game;

import net.minecraft.network.RegistryFriendlyByteBuf;

public interface ClientboundPlayerInfoUpdatePacket$Action$Writer {
   void write(RegistryFriendlyByteBuf output, ClientboundPlayerInfoUpdatePacket$Entry entry);
}
