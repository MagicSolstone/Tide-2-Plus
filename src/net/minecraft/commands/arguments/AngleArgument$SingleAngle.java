package net.minecraft.commands.arguments;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.util.Mth;

public final class AngleArgument$SingleAngle {
   private final float angle;
   private final boolean isRelative;

   private AngleArgument$SingleAngle(final float angle, final boolean isRelative) {
      this.angle = angle;
      this.isRelative = isRelative;
   }

   public float getAngle(final CommandSourceStack sender) {
      return Mth.wrapDegrees(this.isRelative ? this.angle + sender.getRotation().y : this.angle);
   }
}
