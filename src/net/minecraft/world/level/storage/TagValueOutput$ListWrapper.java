package net.minecraft.world.level.storage;

import com.mojang.serialization.DynamicOps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.util.ProblemReporter$IndexedFieldPathElement;

class TagValueOutput$ListWrapper implements ValueOutput$ValueOutputList {
   private final String fieldName;
   private final ProblemReporter problemReporter;
   private final DynamicOps<Tag> ops;
   private final ListTag output;

   private TagValueOutput$ListWrapper(final String fieldName, final ProblemReporter problemReporter, final DynamicOps<Tag> ops, final ListTag output) {
      this.fieldName = fieldName;
      this.problemReporter = problemReporter;
      this.ops = ops;
      this.output = output;
   }

   @Override
   public ValueOutput addChild() {
      int newChildIndex = this.output.size();
      CompoundTag child = new CompoundTag();
      this.output.add(child);
      return new TagValueOutput(this.problemReporter.forChild(new ProblemReporter$IndexedFieldPathElement(this.fieldName, newChildIndex)), this.ops, child);
   }

   @Override
   public void discardLast() {
      this.output.removeLast();
   }

   @Override
   public boolean isEmpty() {
      return this.output.isEmpty();
   }
}
