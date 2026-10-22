package net.minecraft.client.renderer.block.dispatch;

import com.mojang.math.Transformation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Direction;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;

@Environment(EnvType.CLIENT)
public interface ModelState {
   Matrix4fc NO_TRANSFORM = new Matrix4f();

   default Transformation transformation() {
      return Transformation.IDENTITY;
   }

   default Matrix4fc faceTransformation(final Direction face) {
      return NO_TRANSFORM;
   }

   default Matrix4fc inverseFaceTransformation(final Direction face) {
      return NO_TRANSFORM;
   }
}
