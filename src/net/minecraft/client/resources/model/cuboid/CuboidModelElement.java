package net.minecraft.client.resources.model.cuboid;

import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Direction;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record CuboidModelElement(
   Vector3fc from,
   Vector3fc to,
   Map<Direction, CuboidFace> faces,
   @Nullable CuboidRotation rotation,
   @Nullable Direction shadeDirectionOverride,
   int lightEmission
) {
   private static final boolean DEFAULT_RESCALE = false;
   private static final float MIN_EXTENT = -16.0F;
   private static final float MAX_EXTENT = 32.0F;

   public CuboidModelElement(final Vector3fc from, final Vector3fc to, final Map<Direction, CuboidFace> faces) {
      this(from, to, faces, null, null, 0);
   }
}
