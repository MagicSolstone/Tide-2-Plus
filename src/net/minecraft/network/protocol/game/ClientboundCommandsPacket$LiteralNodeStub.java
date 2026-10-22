package net.minecraft.network.protocol.game;

import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.network.FriendlyByteBuf;

record ClientboundCommandsPacket$LiteralNodeStub(String id) implements ClientboundCommandsPacket$NodeStub {
   @Override
   public <S> ArgumentBuilder<S, ?> build(final CommandBuildContext context, final ClientboundCommandsPacket$NodeBuilder<S> builder) {
      return builder.createLiteral(this.id);
   }

   @Override
   public void write(final FriendlyByteBuf output) {
      output.writeUtf(this.id);
   }
}
