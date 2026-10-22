package net.minecraft.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.nbt.CollectionTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import org.apache.commons.lang3.mutable.MutableBoolean;

public class NbtPathArgument$NbtPath {
   private final String original;
   private final Object2IntMap<NbtPathArgument$Node> nodeToOriginalPosition;
   private final NbtPathArgument$Node[] nodes;
   public static final Codec<NbtPathArgument$NbtPath> CODEC = Codec.STRING.comapFlatMap(string -> {
      try {
         NbtPathArgument$NbtPath parsed = new NbtPathArgument().parse(new StringReader(string));
         return DataResult.success(parsed);
      } catch (CommandSyntaxException var2) {
         return DataResult.error(() -> "Failed to parse path " + string + ": " + var2.getMessage());
      }
   }, NbtPathArgument$NbtPath::asString);

   public static NbtPathArgument$NbtPath of(final String string) throws CommandSyntaxException {
      return new NbtPathArgument().parse(new StringReader(string));
   }

   public NbtPathArgument$NbtPath(final String original, final NbtPathArgument$Node[] nodes, final Object2IntMap<NbtPathArgument$Node> nodeToOriginalPosition) {
      this.original = original;
      this.nodes = nodes;
      this.nodeToOriginalPosition = nodeToOriginalPosition;
   }

   public List<Tag> get(final Tag tag) throws CommandSyntaxException {
      List<Tag> result = Collections.singletonList(tag);

      for (NbtPathArgument$Node node : this.nodes) {
         result = node.get(result);
         if (result.isEmpty()) {
            throw this.createNotFoundException(node);
         }
      }

      return result;
   }

   public int countMatching(final Tag tag) {
      List<Tag> result = Collections.singletonList(tag);

      for (NbtPathArgument$Node node : this.nodes) {
         result = node.get(result);
         if (result.isEmpty()) {
            return 0;
         }
      }

      return result.size();
   }

   private List<Tag> getOrCreateParents(final Tag tag) throws CommandSyntaxException {
      List<Tag> result = Collections.singletonList(tag);

      for (int i = 0; i < this.nodes.length - 1; i++) {
         NbtPathArgument$Node node = this.nodes[i];
         int next = i + 1;
         result = node.getOrCreate(result, this.nodes[next]::createPreferredParentTag);
         if (result.isEmpty()) {
            throw this.createNotFoundException(node);
         }
      }

      return result;
   }

   public List<Tag> getOrCreate(final Tag tag, final Supplier<Tag> newTagValue) throws CommandSyntaxException {
      List<Tag> result = this.getOrCreateParents(tag);
      NbtPathArgument$Node lastNode = this.nodes[this.nodes.length - 1];
      return lastNode.getOrCreate(result, newTagValue);
   }

   private static int apply(final List<Tag> targets, final Function<Tag, Integer> operation) {
      return targets.stream().map(operation).reduce(0, (a, b) -> a + b);
   }

   public static boolean isTooDeep(final Tag tag, final int depth) {
      if (depth >= 512) {
         return true;
      } else {
         if (tag instanceof CompoundTag compound) {
            for (Tag child : compound.values()) {
               if (isTooDeep(child, depth + 1)) {
                  return true;
               }
            }
         } else if (tag instanceof ListTag) {
            for (Tag listEntry : (ListTag)tag) {
               if (isTooDeep(listEntry, depth + 1)) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public int set(final Tag tag, final Tag toAdd) throws CommandSyntaxException {
      if (isTooDeep(toAdd, this.estimatePathDepth())) {
         throw NbtPathArgument.ERROR_DATA_TOO_DEEP.create();
      } else {
         Tag firstCopy = toAdd.copy();
         List<Tag> result = this.getOrCreateParents(tag);
         if (result.isEmpty()) {
            return 0;
         } else {
            NbtPathArgument$Node lastNode = this.nodes[this.nodes.length - 1];
            MutableBoolean usedFirstCopy = new MutableBoolean(false);
            return apply(result, t -> lastNode.setTag(t, () -> {
               if (usedFirstCopy.isFalse()) {
                  usedFirstCopy.setTrue();
                  return firstCopy;
               } else {
                  return firstCopy.copy();
               }
            }));
         }
      }
   }

   private int estimatePathDepth() {
      return this.nodes.length;
   }

   public int insert(final int index, final CompoundTag target, final List<Tag> toInsert) throws CommandSyntaxException {
      List<Tag> toInsertCopy = new ArrayList<>(toInsert.size());

      for (Tag tag : toInsert) {
         Tag copy = tag.copy();
         toInsertCopy.add(copy);
         if (isTooDeep(copy, this.estimatePathDepth())) {
            throw NbtPathArgument.ERROR_DATA_TOO_DEEP.create();
         }
      }

      Collection<Tag> targets = this.getOrCreate(target, ListTag::new);
      int modifiedCount = 0;
      boolean usedFirst = false;

      for (Tag targetTag : targets) {
         if (!(targetTag instanceof CollectionTag targetList)) {
            throw NbtPathArgument.ERROR_EXPECTED_LIST.create(targetTag);
         }

         boolean modified = false;
         int actualIndex = index < 0 ? targetList.size() + index + 1 : index;

         for (Tag sourceTag : toInsertCopy) {
            try {
               if (targetList.addTag(actualIndex, usedFirst ? sourceTag.copy() : sourceTag)) {
                  actualIndex++;
                  modified = true;
               }
            } catch (IndexOutOfBoundsException var16) {
               throw NbtPathArgument.ERROR_INVALID_INDEX.create(actualIndex);
            }
         }

         usedFirst = true;
         modifiedCount += modified ? 1 : 0;
      }

      return modifiedCount;
   }

   public int remove(final Tag tag) {
      List<Tag> result = Collections.singletonList(tag);

      for (int i = 0; i < this.nodes.length - 1; i++) {
         result = this.nodes[i].get(result);
      }

      NbtPathArgument$Node lastNode = this.nodes[this.nodes.length - 1];
      return apply(result, lastNode::removeTag);
   }

   private CommandSyntaxException createNotFoundException(final NbtPathArgument$Node node) {
      int index = this.nodeToOriginalPosition.getInt(node);
      return NbtPathArgument.ERROR_NOTHING_FOUND.create(this.original.substring(0, index));
   }

   @Override
   public String toString() {
      return this.original;
   }

   public String asString() {
      return this.original;
   }
}
