package net.minecraft.client.searchtree;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class IdentifierSearchTree$1 implements IdentifierSearchTree<T> {
   @Override
   public List<T> searchNamespace(final String namespace) {
      return List.of();
   }

   @Override
   public List<T> searchPath(final String path) {
      return List.of();
   }
}
