package net.minecraft.commands.arguments;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

class NbtPathArgument$MatchRootObjectNode implements NbtPathArgument$Node {
   private final Predicate<Tag> predicate;

   public NbtPathArgument$MatchRootObjectNode(final CompoundTag pattern) {
      this.predicate = NbtPathArgument.createTagPredicate(pattern);
   }

   @Override
   public void getTag(final Tag self, final List<Tag> output) {
      if (self instanceof CompoundTag && this.predicate.test(self)) {
         output.add(self);
      }
   }

   @Override
   public void getOrCreateTag(final Tag self, final Supplier<Tag> child, final List<Tag> output) {
      this.getTag(self, output);
   }

   @Override
   public Tag createPreferredParentTag() {
      return new CompoundTag();
   }

   @Override
   public int setTag(final Tag parent, final Supplier<Tag> toAdd) {
      return 0;
   }

   @Override
   public int removeTag(final Tag parent) {
      return 0;
   }
}
