package net.minecraft.network.protocol;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.PacketListener;
import net.minecraft.network.ProtocolInfo;
import net.minecraft.network.codec.StreamCodec;
import org.jspecify.annotations.Nullable;

record ProtocolInfoBuilder$Implementation<L extends PacketListener>(
   ConnectionProtocol id, PacketFlow flow, StreamCodec<ByteBuf, Packet<? super L>> codec, @Nullable BundlerInfo bundlerInfo
) implements ProtocolInfo<L> {
}
