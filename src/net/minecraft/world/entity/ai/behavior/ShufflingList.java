package net.minecraft.world.entity.ai.behavior;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.util.RandomSource;

public class ShufflingList<U> implements Iterable<U> {
   protected final List<ShufflingList$WeightedEntry<U>> entries;
   private final RandomSource random = RandomSource.create();

   public ShufflingList() {
      this.entries = Lists.newArrayList();
   }

   private ShufflingList(final List<ShufflingList$WeightedEntry<U>> entries) {
      this.entries = Lists.newArrayList(entries);
   }

   public static <U> Codec<ShufflingList<U>> codec(final Codec<U> elementCodec) {
      return ShufflingList$WeightedEntry.codec(elementCodec).listOf().xmap(ShufflingList::new, l -> l.entries);
   }

   public ShufflingList<U> add(final U data, final int weight) {
      this.entries.add(new ShufflingList$WeightedEntry<>(data, weight));
      return this;
   }

   public ShufflingList<U> shuffle() {
      this.entries.forEach(k -> k.setRandom(this.random.nextFloat()));
      this.entries.sort(Comparator.comparingDouble(ShufflingList$WeightedEntry::getRandWeight));
      return this;
   }

   public Stream<U> stream() {
      return this.entries.stream().map(ShufflingList$WeightedEntry::getData);
   }

   @Override
   public Iterator<U> iterator() {
      return Iterators.transform(this.entries.iterator(), ShufflingList$WeightedEntry::getData);
   }

   @Override
   public String toString() {
      return "ShufflingList[" + this.entries + "]";
   }
}
