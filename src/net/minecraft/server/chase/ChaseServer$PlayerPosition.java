package net.minecraft.server.chase;

import java.util.Locale;

record ChaseServer$PlayerPosition(String dimensionName, double x, double y, double z, float yRot, float xRot) {
   private String format() {
      return String.format(Locale.ROOT, "t %s %.2f %.2f %.2f %.2f %.2f\n", this.dimensionName, this.x, this.y, this.z, this.yRot, this.xRot);
   }
}
