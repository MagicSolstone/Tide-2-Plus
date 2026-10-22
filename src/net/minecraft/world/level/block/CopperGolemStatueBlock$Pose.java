package net.minecraft.world.level.block;

import com.mojang.serialization.Codec;
import java.util.function.IntFunction;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.ByIdMap$OutOfBoundsStrategy;
import net.minecraft.util.StringRepresentable;

public enum CopperGolemStatueBlock$Pose implements StringRepresentable {
   STANDING("standing"),
   SITTING("sitting"),
   RUNNING("running"),
   STAR("star");

   public static final IntFunction<CopperGolemStatueBlock$Pose> BY_ID = ByIdMap.continuous(Enum::ordinal, values(), ByIdMap$OutOfBoundsStrategy.ZERO);
   public static final Codec<CopperGolemStatueBlock$Pose> CODEC = StringRepresentable.fromEnum(CopperGolemStatueBlock$Pose::values);
   private final String name;

   private CopperGolemStatueBlock$Pose(final String name) {
      this.name = name;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }

   public CopperGolemStatueBlock$Pose getNextPose() {
      return BY_ID.apply(this.ordinal() + 1);
   }
}
