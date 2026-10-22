package net.minecraft.client.searchtree;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class IdentifierSearchTree$2 implements IdentifierSearchTree<T> {
   IdentifierSearchTree$2(final SuffixArray val$namespaceTree, final SuffixArray val$pathTree) {
      this.val$namespaceTree = val$namespaceTreex;
      this.val$pathTree = val$pathTreex;
   }

   @Override
   public List<T> searchNamespace(final String namespace) {
      return this.val$namespaceTree.search(namespace);
   }

   @Override
   public List<T> searchPath(final String path) {
      return this.val$pathTree.search(path);
   }
}
