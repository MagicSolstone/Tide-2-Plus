package net.minecraft.world.level.biome;

import java.util.List;
import org.jspecify.annotations.Nullable;

final class Climate$RTree$SubTree<T> extends Climate$RTree$Node<T> {
   private final Climate$RTree$Node<T>[] children;

   public Climate$RTree$SubTree(final List<? extends Climate$RTree$Node<T>> children) {
      this(Climate$RTree.buildParameterSpace(children), children);
   }

   public Climate$RTree$SubTree(final List<Climate$Parameter> parameterSpace, final List<? extends Climate$RTree$Node<T>> children) {
      super(parameterSpace);
      this.children = children.toArray(new Climate$RTree$Node[0]);
   }

   @Override
   protected Climate$RTree$Leaf<T> search(final long[] target, @Nullable final Climate$RTree$Leaf<T> candidate, final Climate$DistanceMetric<T> distanceMetric) {
      long minDistance = candidate == null ? Long.MAX_VALUE : distanceMetric.distance(candidate, target);
      Climate$RTree$Leaf<T> closestLeaf = candidate;

      for (Climate$RTree$Node<T> child : this.children) {
         long childDistance = distanceMetric.distance(child, target);
         if (minDistance > childDistance) {
            Climate$RTree$Leaf<T> leaf = child.search(target, closestLeaf, distanceMetric);
            long leafDistance = child == leaf ? childDistance : distanceMetric.distance(leaf, target);
            if (minDistance > leafDistance) {
               minDistance = leafDistance;
               closestLeaf = leaf;
            }
         }
      }

      return closestLeaf;
   }
}
