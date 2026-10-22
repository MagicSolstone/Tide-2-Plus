package net.minecraft.commands.arguments;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import org.apache.commons.lang3.mutable.MutableBoolean;

class NbtPathArgument$MatchElementNode implements NbtPathArgument$Node {
   private final CompoundTag pattern;
   private final Predicate<Tag> predicate;

   public NbtPathArgument$MatchElementNode(final CompoundTag pattern) {
      this.pattern = pattern;
      this.predicate = NbtPathArgument.createTagPredicate(pattern);
   }

   @Override
   public void getTag(final Tag parent, final List<Tag> output) {
      if (parent instanceof ListTag list) {
         list.stream().filter(this.predicate).forEach(output::add);
      }
   }

   @Override
   public void getOrCreateTag(final Tag parent, final Supplier<Tag> child, final List<Tag> output) {
      MutableBoolean foundAnything = new MutableBoolean();
      if (parent instanceof ListTag list) {
         list.stream().filter(this.predicate).forEach(t -> {
            output.add(t);
            foundAnything.setTrue();
         });
         if (foundAnything.isFalse()) {
            CompoundTag newTag = this.pattern.copy();
            list.add(newTag);
            output.add(newTag);
         }
      }
   }

   @Override
   public Tag createPreferredParentTag() {
      return new ListTag();
   }

   @Override
   public int setTag(final Tag parent, final Supplier<Tag> toAdd) {
      int changedCount = 0;
      if (parent instanceof ListTag list) {
         int size = list.size();
         if (size == 0) {
            list.add(toAdd.get());
            changedCount++;
         } else {
            for (int i = 0; i < size; i++) {
               Tag currentValue = list.get(i);
               if (this.predicate.test(currentValue)) {
                  Tag newValue = toAdd.get();
                  if (!newValue.equals(currentValue) && list.setTag(i, newValue)) {
                     changedCount++;
                  }
               }
            }
         }
      }

      return changedCount;
   }

   @Override
   public int removeTag(final Tag parent) {
      int changedCount = 0;
      if (parent instanceof ListTag list) {
         for (int i = list.size() - 1; i >= 0; i--) {
            if (this.predicate.test(list.get(i))) {
               list.remove(i);
               changedCount++;
            }
         }
      }

      return changedCount;
   }
}
