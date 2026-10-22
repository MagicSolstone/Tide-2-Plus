package net.minecraft.world.level.pathfinder;

import com.mojang.datafixers.util.Function3;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class Node {
   public static final StreamCodec<ByteBuf, Node> DEBUG_STREAM_CODEC = createDebugStreamCodec(Node::new);
   public final int x;
   public final int y;
   public final int z;
   private final int hash;
   public int heapIdx = -1;
   public float g;
   public float h;
   public float f;
   @Nullable
   public Node cameFrom;
   public boolean closed;
   public float walkedDistance;
   public float costMalus;
   public PathType type = PathType.BLOCKED;

   protected static <N extends Node> StreamCodec<ByteBuf, N> createDebugStreamCodec(final Function3<Integer, Integer, Integer, N> factory) {
      return StreamCodec.composite(
         ByteBufCodecs.INT,
         n -> n.x,
         ByteBufCodecs.INT,
         n -> n.y,
         ByteBufCodecs.INT,
         n -> n.z,
         ByteBufCodecs.FLOAT,
         n -> n.walkedDistance,
         ByteBufCodecs.FLOAT,
         n -> n.costMalus,
         ByteBufCodecs.BOOL,
         n -> n.closed,
         PathType.STREAM_CODEC,
         n -> n.type,
         ByteBufCodecs.FLOAT,
         n -> n.f,
         (x, y, z, walkedDistance, costMalus, closed, type, f) -> {
            N node = (N)factory.apply(x, y, z);
            node.walkedDistance = walkedDistance;
            node.costMalus = costMalus;
            node.closed = closed;
            node.type = type;
            node.f = f;
            return node;
         }
      );
   }

   public Node(final int x, final int y, final int z) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.hash = createHash(x, y, z);
   }

   public Node cloneAndMove(final int x, final int y, final int z) {
      Node node = new Node(x, y, z);
      node.heapIdx = this.heapIdx;
      node.g = this.g;
      node.h = this.h;
      node.f = this.f;
      node.cameFrom = this.cameFrom;
      node.closed = this.closed;
      node.walkedDistance = this.walkedDistance;
      node.costMalus = this.costMalus;
      node.type = this.type;
      return node;
   }

   public static int createHash(final int x, final int y, final int z) {
      return y & 0xFF | (x & 32767) << 8 | (z & 32767) << 24 | (x < 0 ? Integer.MIN_VALUE : 0) | (z < 0 ? 32768 : 0);
   }

   public float distanceTo(final Node to) {
      float xd = to.x - this.x;
      float yd = to.y - this.y;
      float zd = to.z - this.z;
      return Mth.sqrt(xd * xd + yd * yd + zd * zd);
   }

   public float distanceToXZ(final Node to) {
      float xd = to.x - this.x;
      float zd = to.z - this.z;
      return Mth.sqrt(xd * xd + zd * zd);
   }

   public float distanceTo(final BlockPos pos) {
      float xd = pos.getX() - this.x;
      float yd = pos.getY() - this.y;
      float zd = pos.getZ() - this.z;
      return Mth.sqrt(xd * xd + yd * yd + zd * zd);
   }

   public float distanceToSqr(final Node to) {
      float xd = to.x - this.x;
      float yd = to.y - this.y;
      float zd = to.z - this.z;
      return xd * xd + yd * yd + zd * zd;
   }

   public float distanceToSqr(final BlockPos pos) {
      float xd = pos.getX() - this.x;
      float yd = pos.getY() - this.y;
      float zd = pos.getZ() - this.z;
      return xd * xd + yd * yd + zd * zd;
   }

   public float distanceManhattan(final Node to) {
      float xd = Math.abs(to.x - this.x);
      float yd = Math.abs(to.y - this.y);
      float zd = Math.abs(to.z - this.z);
      return xd + yd + zd;
   }

   public float distanceManhattan(final BlockPos pos) {
      float xd = Math.abs(pos.getX() - this.x);
      float yd = Math.abs(pos.getY() - this.y);
      float zd = Math.abs(pos.getZ() - this.z);
      return xd + yd + zd;
   }

   public BlockPos asBlockPos() {
      return new BlockPos(this.x, this.y, this.z);
   }

   public Vec3 asVec3() {
      return new Vec3(this.x, this.y, this.z);
   }

   @Override
   public boolean equals(final Object o) {
      return !(o instanceof Node no) ? false : this.hash == no.hash && this.x == no.x && this.y == no.y && this.z == no.z;
   }

   @Override
   public int hashCode() {
      return this.hash;
   }

   public boolean inOpenSet() {
      return this.heapIdx >= 0;
   }

   @Override
   public String toString() {
      return "Node{x=" + this.x + ", y=" + this.y + ", z=" + this.z + "}";
   }
}
