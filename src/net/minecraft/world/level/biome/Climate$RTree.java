package net.minecraft.world.level.biome;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public final class Climate$RTree<T> {
   private static final int CHILDREN_PER_NODE = 19;
   private final Climate$RTree$Node<T> root;
   private final ThreadLocal<Climate$RTree$Leaf<T>> lastResult = new ThreadLocal<>();

   private Climate$RTree(final Climate$RTree$Node<T> root) {
      this.root = root;
   }

   public static <T> Climate$RTree<T> create(final List<Pair<Climate$ParameterPoint, T>> values) {
      return create(values, 19);
   }

   public static <T> Climate$RTree<T> create(final List<Pair<Climate$ParameterPoint, T>> values, final int childrenPerNode) {
      if (values.isEmpty()) {
         throw new IllegalArgumentException("Need at least one value to build the search tree.");
      } else {
         int dimensions = ((Climate$ParameterPoint)values.get(0).getFirst()).parameterSpace().size();
         if (dimensions != 7) {
            throw new IllegalStateException("Expecting parameter space to be 7, got " + dimensions);
         } else {
            List<Climate$RTree$Leaf<T>> leaves = values.stream()
               .map(p -> new Climate$RTree$Leaf<>((Climate$ParameterPoint)p.getFirst(), p.getSecond()))
               .collect(Collectors.toCollection(ArrayList::new));
            return new Climate$RTree<>(build(dimensions, leaves, childrenPerNode));
         }
      }
   }

   private static <T> Climate$RTree$Node<T> build(final int dimensions, final List<? extends Climate$RTree$Node<T>> children, final int childrenPerNode) {
      if (children.isEmpty()) {
         throw new IllegalStateException("Need at least one child to build a node");
      } else if (children.size() == 1) {
         return (Climate$RTree$Node<T>)children.get(0);
      } else if (children.size() <= childrenPerNode) {
         children.sort(Comparator.comparingLong(leaf -> {
            long totalMagnitude = 0L;

            for (int dx = 0; dx < dimensions; dx++) {
               Climate$Parameter parameter = leaf.parameterSpace[dx];
               totalMagnitude += Math.abs((parameter.min() + parameter.max()) / 2L);
            }

            return totalMagnitude;
         }));
         return new Climate$RTree$SubTree<>(children);
      } else {
         long minCost = Long.MAX_VALUE;
         int minDimension = -1;
         List<Climate$RTree$SubTree<T>> minBuckets = null;

         for (int d = 0; d < dimensions; d++) {
            sort(children, dimensions, d, false);
            List<Climate$RTree$SubTree<T>> buckets = bucketize(children, childrenPerNode);
            long totalCost = 0L;

            for (Climate$RTree$SubTree<T> bucket : buckets) {
               totalCost += cost(bucket.parameterSpace);
            }

            if (minCost > totalCost) {
               minCost = totalCost;
               minDimension = d;
               minBuckets = buckets;
            }
         }

         sort(minBuckets, dimensions, minDimension, true);
         return new Climate$RTree$SubTree<>(
            minBuckets.stream().map(b -> build(dimensions, Arrays.asList(b.children), childrenPerNode)).collect(Collectors.toList())
         );
      }
   }

   private static <T> void sort(final List<? extends Climate$RTree$Node<T>> children, final int dimensions, final int dimension, final boolean absolute) {
      Comparator<Climate$RTree$Node<T>> comparator = comparator(dimension, absolute);

      for (int d = 1; d < dimensions; d++) {
         comparator = comparator.thenComparing(comparator((dimension + d) % dimensions, absolute));
      }

      children.sort(comparator);
   }

   private static <T> Comparator<Climate$RTree$Node<T>> comparator(final int dimension, final boolean absolute) {
      return Comparator.comparingLong(leaf -> {
         Climate$Parameter parameter = leaf.parameterSpace[dimension];
         long center = (parameter.min() + parameter.max()) / 2L;
         return absolute ? Math.abs(center) : center;
      });
   }

   private static <T> List<Climate$RTree$SubTree<T>> bucketize(final List<? extends Climate$RTree$Node<T>> nodes, final int childrenPerNode) {
      List<Climate$RTree$SubTree<T>> buckets = Lists.newArrayList();
      List<Climate$RTree$Node<T>> children = Lists.newArrayList();
      int expectedChildrenCount = (int)Math.pow(childrenPerNode, Math.floor(Math.log(nodes.size() - 0.01) / Math.log(childrenPerNode)));

      for (Climate$RTree$Node<T> child : nodes) {
         children.add(child);
         if (children.size() >= expectedChildrenCount) {
            buckets.add(new Climate$RTree$SubTree<>(children));
            children = Lists.newArrayList();
         }
      }

      if (!children.isEmpty()) {
         buckets.add(new Climate$RTree$SubTree<>(children));
      }

      return buckets;
   }

   private static long cost(final Climate$Parameter[] parameterSpace) {
      long result = 0L;

      for (Climate$Parameter parameter : parameterSpace) {
         result += Math.abs(parameter.max() - parameter.min());
      }

      return result;
   }

   private static <T> List<Climate$Parameter> buildParameterSpace(final List<? extends Climate$RTree$Node<T>> children) {
      if (children.isEmpty()) {
         throw new IllegalArgumentException("SubTree needs at least one child");
      } else {
         int dimensions = 7;
         List<Climate$Parameter> bounds = Lists.newArrayList();

         for (int d = 0; d < 7; d++) {
            bounds.add(null);
         }

         for (Climate$RTree$Node<T> child : children) {
            for (int d = 0; d < 7; d++) {
               bounds.set(d, child.parameterSpace[d].span(bounds.get(d)));
            }
         }

         return bounds;
      }
   }

   public T search(final Climate$TargetPoint target, final Climate$DistanceMetric<T> distanceMetric) {
      long[] targetArray = target.toParameterArray();
      Climate$RTree$Leaf<T> leaf = this.root.search(targetArray, this.lastResult.get(), distanceMetric);
      this.lastResult.set(leaf);
      return leaf.value;
   }
}
