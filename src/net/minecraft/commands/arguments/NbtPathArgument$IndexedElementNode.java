package net.minecraft.commands.arguments;

import java.util.List;
import java.util.function.Supplier;
import net.minecraft.nbt.CollectionTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

class NbtPathArgument$IndexedElementNode implements NbtPathArgument$Node {
   private final int index;

   public NbtPathArgument$IndexedElementNode(final int index) {
      this.index = index;
   }

   @Override
   public void getTag(final Tag parent, final List<Tag> output) {
      if (parent instanceof CollectionTag list) {
         int size = list.size();
         int actualIndex = this.index < 0 ? size + this.index : this.index;
         if (0 <= actualIndex && actualIndex < size) {
            output.add(list.get(actualIndex));
         }
      }
   }

   @Override
   public void getOrCreateTag(final Tag parent, final Supplier<Tag> child, final List<Tag> output) {
      this.getTag(parent, output);
   }

   @Override
   public Tag createPreferredParentTag() {
      return new ListTag();
   }

   @Override
   public int setTag(final Tag parent, final Supplier<Tag> toAdd) {
      if (parent instanceof CollectionTag list) {
         int size = list.size();
         int actualIndex = this.index < 0 ? size + this.index : this.index;
         if (0 <= actualIndex && actualIndex < size) {
            Tag previousValue = list.get(actualIndex);
            Tag newValue = toAdd.get();
            if (!newValue.equals(previousValue) && list.setTag(actualIndex, newValue)) {
               return 1;
            }
         }
      }

      return 0;
   }

   @Override
   public int removeTag(final Tag parent) {
      if (parent instanceof CollectionTag list) {
         int size = list.size();
         int actualIndex = this.index < 0 ? size + this.index : this.index;
         if (0 <= actualIndex && actualIndex < size) {
            list.remove(actualIndex);
            return 1;
         }
      }

      return 0;
   }
}
