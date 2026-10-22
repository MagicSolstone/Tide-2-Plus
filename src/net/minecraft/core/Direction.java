package net.minecraft.core;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.netty.buffer.ByteBuf;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.function.IntFunction;
import java.util.stream.Stream;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.ByIdMap$OutOfBoundsStrategy;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.StringRepresentable$EnumCodec;
import net.minecraft.util.Util;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Contract;
import org.joml.Matrix4fc;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

public enum Direction implements Directional, StringRepresentable {
   DOWN(0, 1, -1, "down", Direction$AxisDirection.NEGATIVE, Direction$Axis.Y, new Vec3i(0, -1, 0)),
   UP(1, 0, -1, "up", Direction$AxisDirection.POSITIVE, Direction$Axis.Y, new Vec3i(0, 1, 0)),
   NORTH(2, 3, 2, "north", Direction$AxisDirection.NEGATIVE, Direction$Axis.Z, new Vec3i(0, 0, -1)),
   SOUTH(3, 2, 0, "south", Direction$AxisDirection.POSITIVE, Direction$Axis.Z, new Vec3i(0, 0, 1)),
   WEST(4, 5, 1, "west", Direction$AxisDirection.NEGATIVE, Direction$Axis.X, new Vec3i(-1, 0, 0)),
   EAST(5, 4, 3, "east", Direction$AxisDirection.POSITIVE, Direction$Axis.X, new Vec3i(1, 0, 0));

   public static final StringRepresentable$EnumCodec<Direction> CODEC = StringRepresentable.fromEnum(Direction::values);
   public static final Codec<Direction> VERTICAL_CODEC = CODEC.validate(Direction::verifyVertical);
   public static final IntFunction<Direction> BY_ID = ByIdMap.continuous(Direction::get3DDataValue, values(), ByIdMap$OutOfBoundsStrategy.WRAP);
   public static final StreamCodec<ByteBuf, Direction> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Direction::get3DDataValue);
   @Deprecated
   public static final Codec<Direction> LEGACY_ID_CODEC = Codec.BYTE.xmap(Direction::from3DDataValue, d -> (byte)d.get3DDataValue());
   @Deprecated
   public static final Codec<Direction> LEGACY_ID_CODEC_2D = Codec.BYTE.xmap(Direction::from2DDataValue, d -> (byte)d.get2DDataValue());
   private static final ImmutableList<Direction$Axis> YXZ_AXIS_ORDER = ImmutableList.of(Direction$Axis.Y, Direction$Axis.X, Direction$Axis.Z);
   private static final ImmutableList<Direction$Axis> YZX_AXIS_ORDER = ImmutableList.of(Direction$Axis.Y, Direction$Axis.Z, Direction$Axis.X);
   private final int data3d;
   private final int oppositeIndex;
   private final int data2d;
   private final String name;
   private final Direction$Axis axis;
   private final Direction$AxisDirection axisDirection;
   private final Vec3i normal;
   private final Vec3 normalVec3;
   private final Vector3fc normalVec3f;
   private static final Direction[] VALUES = values();
   private static final Direction[] BY_3D_DATA = Arrays.stream(VALUES).sorted(Comparator.comparingInt(d -> d.data3d)).toArray(Direction[]::new);
   private static final Direction[] BY_2D_DATA = Arrays.stream(VALUES)
      .filter(d -> d.getAxis().isHorizontal())
      .sorted(Comparator.comparingInt(d -> d.data2d))
      .toArray(Direction[]::new);

   private Direction(
      final int data3d,
      final int oppositeIndex,
      final int data2d,
      final String name,
      final Direction$AxisDirection axisDirection,
      final Direction$Axis axis,
      final Vec3i normal
   ) {
      this.data3d = data3d;
      this.data2d = data2d;
      this.oppositeIndex = oppositeIndex;
      this.name = name;
      this.axis = axis;
      this.axisDirection = axisDirection;
      this.normal = normal;
      this.normalVec3 = Vec3.atLowerCornerOf(normal);
      this.normalVec3f = new Vector3f(normal.getX(), normal.getY(), normal.getZ());
   }

   public static Direction[] orderedByNearest(final Entity entity) {
      float pitch = entity.getViewXRot(1.0F) * (float) (Math.PI / 180.0);
      float yaw = -entity.getViewYRot(1.0F) * (float) (Math.PI / 180.0);
      float pitchSin = Mth.sin(pitch);
      float pitchCos = Mth.cos(pitch);
      float yawSin = Mth.sin(yaw);
      float yawCos = Mth.cos(yaw);
      boolean xPos = yawSin > 0.0F;
      boolean yPos = pitchSin < 0.0F;
      boolean zPos = yawCos > 0.0F;
      float xYaw = xPos ? yawSin : -yawSin;
      float yMag = yPos ? -pitchSin : pitchSin;
      float zYaw = zPos ? yawCos : -yawCos;
      float xMag = xYaw * pitchCos;
      float zMag = zYaw * pitchCos;
      Direction axisX = xPos ? EAST : WEST;
      Direction axisY = yPos ? UP : DOWN;
      Direction axisZ = zPos ? SOUTH : NORTH;
      if (xYaw > zYaw) {
         if (yMag > xMag) {
            return makeDirectionArray(axisY, axisX, axisZ);
         } else {
            return zMag > yMag ? makeDirectionArray(axisX, axisZ, axisY) : makeDirectionArray(axisX, axisY, axisZ);
         }
      } else if (yMag > zMag) {
         return makeDirectionArray(axisY, axisZ, axisX);
      } else {
         return xMag > yMag ? makeDirectionArray(axisZ, axisX, axisY) : makeDirectionArray(axisZ, axisY, axisX);
      }
   }

   private static Direction[] makeDirectionArray(final Direction axis1, final Direction axis2, final Direction axis3) {
      return new Direction[]{axis1, axis2, axis3, axis3.getOpposite(), axis2.getOpposite(), axis1.getOpposite()};
   }

   public static Direction rotate(final Matrix4fc matrix, final Direction facing) {
      Vector3f vec = matrix.transformDirection(facing.normalVec3f, new Vector3f());
      return getApproximateNearest(vec.x(), vec.y(), vec.z());
   }

   public static Collection<Direction> allShuffled(final RandomSource random) {
      return Util.shuffledCopy(values(), random);
   }

   public static Stream<Direction> stream() {
      return Stream.of(VALUES);
   }

   public static float getYRot(final Direction direction) {
      return switch (direction) {
         case NORTH -> 180.0F;
         case SOUTH -> 0.0F;
         case WEST -> 90.0F;
         case EAST -> -90.0F;
         default -> throw new IllegalStateException("No y-Rot for vertical axis: " + direction);
      };
   }

   public Quaternionf getRotation() {
      return switch (this) {
         case DOWN -> new Quaternionf().rotationX((float) Math.PI);
         case UP -> new Quaternionf();
         case NORTH -> new Quaternionf().rotationXYZ((float) (Math.PI / 2), 0.0F, (float) Math.PI);
         case SOUTH -> new Quaternionf().rotationX((float) (Math.PI / 2));
         case WEST -> new Quaternionf().rotationXYZ((float) (Math.PI / 2), 0.0F, (float) (Math.PI / 2));
         case EAST -> new Quaternionf().rotationXYZ((float) (Math.PI / 2), 0.0F, (float) (-Math.PI / 2));
      };
   }

   public int get3DDataValue() {
      return this.data3d;
   }

   public int get2DDataValue() {
      return this.data2d;
   }

   public Direction$AxisDirection getAxisDirection() {
      return this.axisDirection;
   }

   public static Direction getFacingAxis(final Entity entity, final Direction$Axis axis) {
      return switch (axis) {
         case X -> EAST.isFacingAngle(entity.getViewYRot(1.0F)) ? EAST : WEST;
         case Y -> entity.getViewXRot(1.0F) < 0.0F ? UP : DOWN;
         case Z -> SOUTH.isFacingAngle(entity.getViewYRot(1.0F)) ? SOUTH : NORTH;
      };
   }

   public Direction getOpposite() {
      return from3DDataValue(this.oppositeIndex);
   }

   public Direction getClockWise(final Direction$Axis axis) {
      return switch (axis) {
         case X -> this != WEST && this != EAST ? this.getClockWiseX() : this;
         case Y -> this != UP && this != DOWN ? this.getClockWise() : this;
         case Z -> this != NORTH && this != SOUTH ? this.getClockWiseZ() : this;
      };
   }

   public Direction getCounterClockWise(final Direction$Axis axis) {
      return switch (axis) {
         case X -> this != WEST && this != EAST ? this.getCounterClockWiseX() : this;
         case Y -> this != UP && this != DOWN ? this.getCounterClockWise() : this;
         case Z -> this != NORTH && this != SOUTH ? this.getCounterClockWiseZ() : this;
      };
   }

   public Direction getClockWise() {
      return switch (this) {
         case NORTH -> EAST;
         case SOUTH -> WEST;
         case WEST -> NORTH;
         case EAST -> SOUTH;
         default -> throw new IllegalStateException("Unable to get Y-rotated facing of " + this);
      };
   }

   private Direction getClockWiseX() {
      return switch (this) {
         case DOWN -> SOUTH;
         case UP -> NORTH;
         case NORTH -> DOWN;
         case SOUTH -> UP;
         default -> throw new IllegalStateException("Unable to get X-rotated facing of " + this);
      };
   }

   private Direction getCounterClockWiseX() {
      return switch (this) {
         case DOWN -> NORTH;
         case UP -> SOUTH;
         case NORTH -> UP;
         case SOUTH -> DOWN;
         default -> throw new IllegalStateException("Unable to get X-rotated facing of " + this);
      };
   }

   private Direction getClockWiseZ() {
      return switch (this) {
         case DOWN -> WEST;
         case UP -> EAST;
         default -> throw new IllegalStateException("Unable to get Z-rotated facing of " + this);
         case WEST -> UP;
         case EAST -> DOWN;
      };
   }

   private Direction getCounterClockWiseZ() {
      return switch (this) {
         case DOWN -> EAST;
         case UP -> WEST;
         default -> throw new IllegalStateException("Unable to get Z-rotated facing of " + this);
         case WEST -> DOWN;
         case EAST -> UP;
      };
   }

   public Direction getCounterClockWise() {
      return switch (this) {
         case NORTH -> WEST;
         case SOUTH -> EAST;
         case WEST -> SOUTH;
         case EAST -> NORTH;
         default -> throw new IllegalStateException("Unable to get CCW facing of " + this);
      };
   }

   @Override
   public int getStepX() {
      return this.normal.getX();
   }

   @Override
   public int getStepY() {
      return this.normal.getY();
   }

   @Override
   public int getStepZ() {
      return this.normal.getZ();
   }

   @Override
   public Vec3i getStep() {
      return this.getUnitVec3i();
   }

   public Vector3f step() {
      return new Vector3f(this.normalVec3f);
   }

   public String getName() {
      return this.name;
   }

   public Direction$Axis getAxis() {
      return this.axis;
   }

   @Nullable
   public static Direction byName(final String name) {
      return CODEC.byName(name);
   }

   public static Direction from3DDataValue(final int data) {
      return BY_3D_DATA[Mth.abs(data % BY_3D_DATA.length)];
   }

   public static Direction from2DDataValue(final int data) {
      return BY_2D_DATA[Mth.abs(data % BY_2D_DATA.length)];
   }

   public static Direction fromYRot(final double yRot) {
      return from2DDataValue(Mth.floor(yRot / 90.0 + 0.5) & 3);
   }

   public static Direction fromAxisAndDirection(final Direction$Axis axis, final Direction$AxisDirection direction) {
      return switch (axis) {
         case X -> direction == Direction$AxisDirection.POSITIVE ? EAST : WEST;
         case Y -> direction == Direction$AxisDirection.POSITIVE ? UP : DOWN;
         case Z -> direction == Direction$AxisDirection.POSITIVE ? SOUTH : NORTH;
      };
   }

   public float toYRot() {
      return (this.data2d & 3) * 90;
   }

   public static Direction getRandom(final RandomSource random) {
      return Util.getRandom(VALUES, random);
   }

   public static Direction getApproximateNearest(final double dx, final double dy, final double dz) {
      return getApproximateNearest((float)dx, (float)dy, (float)dz);
   }

   public static Direction getApproximateNearest(final float dx, final float dy, final float dz) {
      Direction result = NORTH;
      float highestDot = Float.MIN_VALUE;

      for (Direction direction : VALUES) {
         float dot = dx * direction.normal.getX() + dy * direction.normal.getY() + dz * direction.normal.getZ();
         if (dot > highestDot) {
            highestDot = dot;
            result = direction;
         }
      }

      return result;
   }

   public static Direction getApproximateNearest(final Vec3 vec) {
      return getApproximateNearest(vec.x, vec.y, vec.z);
   }

   @Contract("_,_,_,!null->!null;_,_,_,_->_")
   @Nullable
   public static Direction getNearest(final int x, final int y, final int z, @Nullable final Direction orElse) {
      int absX = Math.abs(x);
      int absY = Math.abs(y);
      int absZ = Math.abs(z);
      if (absX > absZ && absX > absY) {
         return x < 0 ? WEST : EAST;
      } else if (absZ > absX && absZ > absY) {
         return z < 0 ? NORTH : SOUTH;
      } else if (absY > absX && absY > absZ) {
         return y < 0 ? DOWN : UP;
      } else {
         return orElse;
      }
   }

   @Contract("_,!null->!null;_,_->_")
   @Nullable
   public static Direction getNearest(final Vec3i vec, @Nullable final Direction orElse) {
      return getNearest(vec.getX(), vec.getY(), vec.getZ(), orElse);
   }

   @Override
   public String toString() {
      return this.name;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }

   private static DataResult<Direction> verifyVertical(final Direction v) {
      return v.getAxis().isVertical() ? DataResult.success(v) : DataResult.error(() -> "Expected a vertical direction");
   }

   public static Direction get(final Direction$AxisDirection axisDirection, final Direction$Axis axis) {
      for (Direction direction : VALUES) {
         if (direction.getAxisDirection() == axisDirection && direction.getAxis() == axis) {
            return direction;
         }
      }

      throw new IllegalArgumentException("No such direction: " + axisDirection + " " + axis);
   }

   public static ImmutableList<Direction$Axis> axisStepOrder(final Vec3 movement) {
      return Math.abs(movement.x) < Math.abs(movement.z) ? YZX_AXIS_ORDER : YXZ_AXIS_ORDER;
   }

   public Vec3i getUnitVec3i() {
      return this.normal;
   }

   public Vec3 getUnitVec3() {
      return this.normalVec3;
   }

   public Vector3fc getUnitVec3f() {
      return this.normalVec3f;
   }

   public boolean isFacingAngle(final float yAngle) {
      float radians = yAngle * (float) (Math.PI / 180.0);
      float dx = -Mth.sin(radians);
      float dz = Mth.cos(radians);
      return this.normal.getX() * dx + this.normal.getZ() * dz > 0.0F;
   }
}
