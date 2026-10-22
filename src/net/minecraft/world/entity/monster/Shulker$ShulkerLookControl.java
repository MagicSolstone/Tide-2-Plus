package net.minecraft.world.entity.monster;

import java.util.Objects;
import java.util.Optional;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.LookControl;
import org.joml.Vector3f;

class Shulker$ShulkerLookControl extends LookControl {
   public Shulker$ShulkerLookControl(final Shulker this$0, final Mob mob) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(mob);
   }

   @Override
   protected void clampHeadRotationToBody() {
   }

   @Override
   protected Optional<Float> getYRotD() {
      Direction attachFace = this.this$0.getAttachFace().getOpposite();
      Vector3f forward = attachFace.getRotation().transform(new Vector3f(Shulker.FORWARD));
      Vec3i upNormal = attachFace.getUnitVec3i();
      Vector3f right = new Vector3f(upNormal.getX(), upNormal.getY(), upNormal.getZ());
      right.cross(forward);
      double xd = this.wantedX - this.mob.getX();
      double yd = this.wantedY - this.mob.getEyeY();
      double zd = this.wantedZ - this.mob.getZ();
      Vector3f out = new Vector3f((float)xd, (float)yd, (float)zd);
      float deltaRight = right.dot(out);
      float deltaForward = forward.dot(out);
      return !(Math.abs(deltaRight) > 1.0E-5F) && !(Math.abs(deltaForward) > 1.0E-5F)
         ? Optional.empty()
         : Optional.of((float)(Mth.atan2(-deltaRight, deltaForward) * 180.0F / (float)Math.PI));
   }

   @Override
   protected Optional<Float> getXRotD() {
      return Optional.of(0.0F);
   }
}
