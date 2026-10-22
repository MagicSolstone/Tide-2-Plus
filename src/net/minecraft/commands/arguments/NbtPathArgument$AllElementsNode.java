package net.minecraft.commands.arguments;

import com.google.common.collect.Iterables;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.nbt.CollectionTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

class NbtPathArgument$AllElementsNode implements NbtPathArgument$Node {
   public static final NbtPathArgument$AllElementsNode INSTANCE = new NbtPathArgument$AllElementsNode();

   private NbtPathArgument$AllElementsNode() {
   }

   @Override
   public void getTag(final Tag parent, final List<Tag> output) {
      if (parent instanceof CollectionTag collection) {
         Iterables.addAll(output, collection);
      }
   }

   @Override
   public void getOrCreateTag(final Tag parent, final Supplier<Tag> child, final List<Tag> output) {
      if (parent instanceof CollectionTag list) {
         if (list.isEmpty()) {
            Tag result = child.get();
            if (list.addTag(0, result)) {
               output.add(result);
            }
         } else {
            Iterables.addAll(output, list);
         }
      }
   }

   @Override
   public Tag createPreferredParentTag() {
      return new ListTag();
   }

   @Override
   public int setTag(final Tag parent, final Supplier<Tag> toAdd) {
      if (!(parent instanceof CollectionTag list)) {
         return 0;
      } else {
         int size = list.size();
         if (size == 0) {
            list.addTag(0, toAdd.get());
            return 1;
         } else {
            Tag newValue = toAdd.get();
            int changedCount = size - (int)list.stream().filter(newValue::equals).count();
            if (changedCount == 0) {
               return 0;
            } else {
               list.clear();
               if (!list.addTag(0, newValue)) {
                  return 0;
               } else {
                  for (int i = 1; i < size; i++) {
                     list.addTag(i, toAdd.get());
                  }

                  return changedCount;
               }
            }
         }
      }
   }

   @Override
   public int removeTag(final Tag parent) {
      if (parent instanceof CollectionTag list) {
         int size = list.size();
         if (size > 0) {
            list.clear();
            return size;
         }
      }

      return 0;
   }
}
