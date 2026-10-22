package net.minecraft.world.entity.player;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

class Input$1 implements StreamCodec<FriendlyByteBuf, Input> {
   public void encode(final FriendlyByteBuf output, final Input value) {
      byte flags = 0;
      flags = (byte)(flags | (value.forward() ? 1 : 0));
      flags = (byte)(flags | (value.backward() ? 2 : 0));
      flags = (byte)(flags | (value.left() ? 4 : 0));
      flags = (byte)(flags | (value.right() ? 8 : 0));
      flags = (byte)(flags | (value.jump() ? 16 : 0));
      flags = (byte)(flags | (value.shift() ? 32 : 0));
      flags = (byte)(flags | (value.sprint() ? 64 : 0));
      output.writeByte(flags);
   }

   public Input decode(final FriendlyByteBuf input) {
      byte flags = input.readByte();
      boolean forward = (flags & 1) != 0;
      boolean backward = (flags & 2) != 0;
      boolean left = (flags & 4) != 0;
      boolean right = (flags & 8) != 0;
      boolean jump = (flags & 16) != 0;
      boolean shift = (flags & 32) != 0;
      boolean sprint = (flags & 64) != 0;
      return new Input(forward, backward, left, right, jump, shift, sprint);
   }
}
