package net.minecraft.client.gui;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fc;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record ActiveTextCollector$Parameters(Matrix3x2fc pose, float opacity, @Nullable ScreenRectangle scissor) {
   public ActiveTextCollector$Parameters(final Matrix3x2fc pose) {
      this(pose, 1.0F, null);
   }

   public ActiveTextCollector$Parameters withPose(final Matrix3x2fc pose) {
      return new ActiveTextCollector$Parameters(pose, this.opacity, this.scissor);
   }

   public ActiveTextCollector$Parameters withScale(final float scale) {
      return this.withPose(this.pose.scale(scale, scale, new Matrix3x2f()));
   }

   public ActiveTextCollector$Parameters withOpacity(final float opacity) {
      return this.opacity == opacity ? this : new ActiveTextCollector$Parameters(this.pose, opacity, this.scissor);
   }

   public ActiveTextCollector$Parameters withScissor(final ScreenRectangle scissor) {
      return scissor.equals(this.scissor) ? this : new ActiveTextCollector$Parameters(this.pose, this.opacity, scissor);
   }

   public ActiveTextCollector$Parameters withScissor(final int left, final int right, final int top, final int bottom) {
      ScreenRectangle newScissor = new ScreenRectangle(left, top, right - left, bottom - top).transformAxisAligned(this.pose);
      if (this.scissor != null) {
         newScissor = Objects.requireNonNullElse(this.scissor.intersection(newScissor), ScreenRectangle.empty());
      }

      return this.withScissor(newScissor);
   }
}
