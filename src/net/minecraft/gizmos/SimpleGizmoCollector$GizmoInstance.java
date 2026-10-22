package net.minecraft.gizmos;

import net.minecraft.util.Mth;
import net.minecraft.util.Util;

public class SimpleGizmoCollector$GizmoInstance implements GizmoProperties {
   private final Gizmo gizmo;
   private boolean isAlwaysOnTop;
   private long startTimeMillis;
   private long expireTimeMillis;
   private boolean shouldFadeOut;

   private SimpleGizmoCollector$GizmoInstance(final Gizmo gizmo) {
      this.gizmo = gizmo;
   }

   @Override
   public GizmoProperties setAlwaysOnTop() {
      this.isAlwaysOnTop = true;
      return this;
   }

   @Override
   public GizmoProperties persistForMillis(final int milliseconds) {
      this.startTimeMillis = Util.getMillis();
      this.expireTimeMillis = this.startTimeMillis + milliseconds;
      return this;
   }

   @Override
   public GizmoProperties fadeOut() {
      this.shouldFadeOut = true;
      return this;
   }

   public float getAlphaMultiplier(final long currentMillis) {
      if (this.shouldFadeOut) {
         long duration = this.expireTimeMillis - this.startTimeMillis;
         long timeSinceStart = currentMillis - this.startTimeMillis;
         return 1.0F - Mth.clamp((float)timeSinceStart / (float)duration, 0.0F, 1.0F);
      } else {
         return 1.0F;
      }
   }

   public boolean isAlwaysOnTop() {
      return this.isAlwaysOnTop;
   }

   public long getExpireTimeMillis() {
      return this.expireTimeMillis;
   }

   public Gizmo gizmo() {
      return this.gizmo;
   }
}
