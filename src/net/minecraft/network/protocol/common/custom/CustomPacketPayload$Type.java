package net.minecraft.network.protocol.common.custom;

import net.minecraft.resources.Identifier;

public record CustomPacketPayload$Type<T extends CustomPacketPayload>(Identifier id) {
}
