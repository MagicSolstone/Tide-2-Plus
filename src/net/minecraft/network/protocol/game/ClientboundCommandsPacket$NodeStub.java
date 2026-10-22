package net.minecraft.network.protocol.game;

import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.network.FriendlyByteBuf;

interface ClientboundCommandsPacket$NodeStub {
   <S> ArgumentBuilder<S, ?> build(CommandBuildContext context, ClientboundCommandsPacket$NodeBuilder<S> builder);

   void write(FriendlyByteBuf output);
}
