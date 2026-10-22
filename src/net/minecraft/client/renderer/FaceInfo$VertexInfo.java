package net.minecraft.client.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Vector3f;
import org.joml.Vector3fc;

@Environment(EnvType.CLIENT)
public record FaceInfo$VertexInfo(FaceInfo$Extent xFace, FaceInfo$Extent yFace, FaceInfo$Extent zFace) {
   public Vector3f select(final Vector3fc min, final Vector3fc max) {
      return new Vector3f(this.xFace.select(min, max), this.yFace.select(min, max), this.zFace.select(min, max));
   }
}
