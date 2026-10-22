package net.minecraft.world.level.biome;

import com.google.common.annotations.VisibleForTesting;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.ExtraCodecs;

public class Climate$ParameterList<T> {
   private final List<Pair<Climate$ParameterPoint, T>> values;
   private final Climate$RTree<T> index;

   public static <T> Codec<Climate$ParameterList<T>> codec(final MapCodec<T> valueCodec) {
      return ExtraCodecs.nonEmptyList(
            RecordCodecBuilder.create(
                  i -> i.group(Climate$ParameterPoint.CODEC.fieldOf("parameters").forGetter(Pair::getFirst), valueCodec.forGetter(Pair::getSecond))
                     .apply(i, Pair::of)
               )
               .listOf()
         )
         .xmap(Climate$ParameterList::new, Climate$ParameterList::values);
   }

   public Climate$ParameterList(final List<Pair<Climate$ParameterPoint, T>> values) {
      this(values, 19);
   }

   private Climate$ParameterList(final List<Pair<Climate$ParameterPoint, T>> values, final int childrenPerNode) {
      this.values = values;
      this.index = Climate$RTree.create(values, childrenPerNode);
   }

   @VisibleForTesting
   public Climate$ParameterList<T> rebuildWithChildrenPerNode(final int childrenPerNode) {
      return new Climate$ParameterList<>(this.values, childrenPerNode);
   }

   public List<Pair<Climate$ParameterPoint, T>> values() {
      return this.values;
   }

   public T findValue(final Climate$TargetPoint target) {
      return this.findValueIndex(target);
   }

   @VisibleForTesting
   public T findValueBruteForce(final Climate$TargetPoint target) {
      Iterator<Pair<Climate$ParameterPoint, T>> iterator = this.values().iterator();
      Pair<Climate$ParameterPoint, T> first = iterator.next();
      long bestFitness = ((Climate$ParameterPoint)first.getFirst()).fitness(target);
      T best = (T)first.getSecond();

      while (iterator.hasNext()) {
         Pair<Climate$ParameterPoint, T> parameter = iterator.next();
         long fitness = ((Climate$ParameterPoint)parameter.getFirst()).fitness(target);
         if (fitness < bestFitness) {
            bestFitness = fitness;
            best = (T)parameter.getSecond();
         }
      }

      return best;
   }

   public T findValueIndex(final Climate$TargetPoint target) {
      return this.findValueIndex(target, Climate$RTree$Node::distance);
   }

   protected T findValueIndex(final Climate$TargetPoint target, final Climate$DistanceMetric<T> distanceMetric) {
      return this.index.search(target, distanceMetric);
   }
}
