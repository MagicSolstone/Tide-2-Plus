package net.minecraft.network.protocol.common.custom;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record CustomPacketPayload$TypeAndCodec<B extends FriendlyByteBuf, T extends CustomPacketPayload>(
   CustomPacketPayload$Type<T> type, StreamCodec<B, T> codec
) {
}
