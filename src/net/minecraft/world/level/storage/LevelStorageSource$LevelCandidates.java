package net.minecraft.world.level.storage;

import java.util.Iterator;
import java.util.List;

public record LevelStorageSource$LevelCandidates(List<LevelStorageSource$LevelDirectory> levels) implements Iterable<LevelStorageSource$LevelDirectory> {
   public boolean isEmpty() {
      return this.levels.isEmpty();
   }

   @Override
   public Iterator<LevelStorageSource$LevelDirectory> iterator() {
      return this.levels.iterator();
   }
}
