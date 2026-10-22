package net.minecraft.network.protocol.game;

import net.minecraft.network.RegistryFriendlyByteBuf;

public interface ClientboundPlayerInfoUpdatePacket$Action$Reader {
   void read(ClientboundPlayerInfoUpdatePacket$EntryBuilder entry, RegistryFriendlyByteBuf input);
}
