package net.minecraft.client.renderer.chunk;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Direction;

@Environment(EnvType.CLIENT)
class CompiledSectionMesh$2 implements SectionMesh {
   @Override
   public boolean facesCanSeeEachother(final Direction direction1, final Direction direction2) {
      return true;
   }
}
