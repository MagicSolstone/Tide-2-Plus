package net.minecraft.world.entity.ai.gossip;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Stream;

class GossipContainer$EntityGossips {
   private final Object2IntMap<GossipType> entries = new Object2IntOpenHashMap();

   private GossipContainer$EntityGossips() {
   }

   public int weightedValue(final Predicate<GossipType> types) {
      return this.entries
         .object2IntEntrySet()
         .stream()
         .filter(e -> types.test((GossipType)e.getKey()))
         .mapToInt(e -> e.getIntValue() * ((GossipType)e.getKey()).weight)
         .sum();
   }

   public Stream<GossipContainer$GossipEntry> unpack(final UUID target) {
      return this.entries.object2IntEntrySet().stream().map(e -> new GossipContainer$GossipEntry(target, (GossipType)e.getKey(), e.getIntValue()));
   }

   public void decay() {
      ObjectIterator<Entry<GossipType>> it = this.entries.object2IntEntrySet().iterator();

      while (it.hasNext()) {
         Entry<GossipType> gossip = (Entry<GossipType>)it.next();
         int newValue = gossip.getIntValue() - ((GossipType)gossip.getKey()).decayPerDay;
         if (newValue < 2) {
            it.remove();
         } else {
            gossip.setValue(newValue);
         }
      }
   }

   public boolean isEmpty() {
      return this.entries.isEmpty();
   }

   public void makeSureValueIsntTooLowOrTooHigh(final GossipType type) {
      int value = this.entries.getInt(type);
      if (value > type.max) {
         this.entries.put(type, type.max);
      }

      if (value < 2) {
         this.remove(type);
      }
   }

   public void remove(final GossipType type) {
      this.entries.removeInt(type);
   }
}
