package net.minecraft.network.protocol.common.custom;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

public interface CustomPacketPayload$FallbackProvider<B extends FriendlyByteBuf> {
   StreamCodec<B, ? extends CustomPacketPayload> create(Identifier typeId);
}
