package net.minecraft.world.entity.player;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record Input(boolean forward, boolean backward, boolean left, boolean right, boolean jump, boolean shift, boolean sprint) {
   private static final byte FLAG_FORWARD = 1;
   private static final byte FLAG_BACKWARD = 2;
   private static final byte FLAG_LEFT = 4;
   private static final byte FLAG_RIGHT = 8;
   private static final byte FLAG_JUMP = 16;
   private static final byte FLAG_SHIFT = 32;
   private static final byte FLAG_SPRINT = 64;
   public static final StreamCodec<FriendlyByteBuf, Input> STREAM_CODEC = new Input$1();
   public static final Input EMPTY = new Input(false, false, false, false, false, false, false);
}
