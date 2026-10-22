package net.minecraft.network.protocol.game;

import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.network.FriendlyByteBuf;
import org.jspecify.annotations.Nullable;

record ClientboundCommandsPacket$Entry(@Nullable ClientboundCommandsPacket$NodeStub stub, int flags, int redirect, int[] children) {
   public void write(final FriendlyByteBuf output) {
      output.writeByte(this.flags);
      output.writeVarIntArray(this.children);
      if ((this.flags & 8) != 0) {
         output.writeVarInt(this.redirect);
      }

      if (this.stub != null) {
         this.stub.write(output);
      }
   }

   public boolean canBuild(final IntSet unbuiltNodes) {
      return (this.flags & 8) != 0 ? !unbuiltNodes.contains(this.redirect) : true;
   }

   public boolean canResolve(final IntSet unresolvedNodes) {
      for (int child : this.children) {
         if (unresolvedNodes.contains(child)) {
            return false;
         }
      }

      return true;
   }
}
