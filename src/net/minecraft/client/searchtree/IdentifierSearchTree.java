package net.minecraft.client.searchtree;

import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Stream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public interface IdentifierSearchTree<T> {
   static <T> IdentifierSearchTree<T> empty() {
      return new IdentifierSearchTree$1();
   }

   static <T> IdentifierSearchTree<T> create(final List<T> elements, final Function<T, Stream<Identifier>> idGetter) {
      if (elements.isEmpty()) {
         return empty();
      } else {
         SuffixArray<T> namespaceTree = new SuffixArray<>();
         SuffixArray<T> pathTree = new SuffixArray<>();

         for (T element : elements) {
            idGetter.apply(element).forEach(elementId -> {
               namespaceTree.add(element, elementId.getNamespace().toLowerCase(Locale.ROOT));
               pathTree.add(element, elementId.getPath().toLowerCase(Locale.ROOT));
            });
         }

         namespaceTree.generate();
         pathTree.generate();
         return new IdentifierSearchTree$2(namespaceTree, pathTree);
      }
   }

   List<T> searchNamespace(String namespace);

   List<T> searchPath(String path);
}
