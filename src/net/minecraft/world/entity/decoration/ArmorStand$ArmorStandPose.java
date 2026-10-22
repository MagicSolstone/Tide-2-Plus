package net.minecraft.world.entity.decoration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Rotations;

public record ArmorStand$ArmorStandPose(Rotations head, Rotations body, Rotations leftArm, Rotations rightArm, Rotations leftLeg, Rotations rightLeg) {
   public static final ArmorStand$ArmorStandPose DEFAULT = new ArmorStand$ArmorStandPose(
      ArmorStand.DEFAULT_HEAD_POSE,
      ArmorStand.DEFAULT_BODY_POSE,
      ArmorStand.DEFAULT_LEFT_ARM_POSE,
      ArmorStand.DEFAULT_RIGHT_ARM_POSE,
      ArmorStand.DEFAULT_LEFT_LEG_POSE,
      ArmorStand.DEFAULT_RIGHT_LEG_POSE
   );
   public static final Codec<ArmorStand$ArmorStandPose> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Rotations.CODEC.optionalFieldOf("Head", ArmorStand.DEFAULT_HEAD_POSE).forGetter(ArmorStand$ArmorStandPose::head),
            Rotations.CODEC.optionalFieldOf("Body", ArmorStand.DEFAULT_BODY_POSE).forGetter(ArmorStand$ArmorStandPose::body),
            Rotations.CODEC.optionalFieldOf("LeftArm", ArmorStand.DEFAULT_LEFT_ARM_POSE).forGetter(ArmorStand$ArmorStandPose::leftArm),
            Rotations.CODEC.optionalFieldOf("RightArm", ArmorStand.DEFAULT_RIGHT_ARM_POSE).forGetter(ArmorStand$ArmorStandPose::rightArm),
            Rotations.CODEC.optionalFieldOf("LeftLeg", ArmorStand.DEFAULT_LEFT_LEG_POSE).forGetter(ArmorStand$ArmorStandPose::leftLeg),
            Rotations.CODEC.optionalFieldOf("RightLeg", ArmorStand.DEFAULT_RIGHT_LEG_POSE).forGetter(ArmorStand$ArmorStandPose::rightLeg)
         )
         .apply(i, ArmorStand$ArmorStandPose::new)
   );
}
