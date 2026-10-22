package net.minecraft.client.renderer.chunk;

import java.util.Collections;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public interface SectionMesh extends AutoCloseable {
   default boolean isDifferentPointOfView(final TranslucencyPointOfView pointOfView) {
      return false;
   }

   default boolean hasRenderableLayers() {
      return false;
   }

   default boolean hasTranslucentGeometry() {
      return false;
   }

   default boolean isEmpty(final ChunkSectionLayer layer) {
      return true;
   }

   default List<BlockEntity> getRenderableBlockEntities() {
      return Collections.emptyList();
   }

   boolean facesCanSeeEachother(Direction direction1, Direction direction2);

   @Nullable
   default SectionMesh$SectionDraw getSectionDraw(final ChunkSectionLayer layer) {
      return null;
   }

   default long getCompileTaskStartTime() {
      return 0L;
   }

   @Override
   default void close() {
   }
}
