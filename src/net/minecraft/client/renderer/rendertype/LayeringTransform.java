package net.minecraft.client.renderer.rendertype;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Matrix4f;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class LayeringTransform {
   private final String name;
   @Nullable
   private final Consumer<Matrix4f> modifier;
   public static final LayeringTransform NO_LAYERING = new LayeringTransform("no_layering", null);
   public static final LayeringTransform VIEW_OFFSET_Z_LAYERING = new LayeringTransform(
      "view_offset_z_layering", modelViewMatrix -> RenderSystem.getProjectionType().applyLayeringTransform(modelViewMatrix, 1.0F)
   );
   public static final LayeringTransform VIEW_OFFSET_Z_LAYERING_FORWARD = new LayeringTransform(
      "view_offset_z_layering_forward", modelViewMatrix -> RenderSystem.getProjectionType().applyLayeringTransform(modelViewMatrix, -1.0F)
   );

   public LayeringTransform(final String name, @Nullable final Consumer<Matrix4f> modifier) {
      this.name = name;
      this.modifier = modifier;
   }

   @Override
   public String toString() {
      return "LayeringTransform[" + this.name + "]";
   }

   @Nullable
   public Consumer<Matrix4f> getModifier() {
      return this.modifier;
   }
}
