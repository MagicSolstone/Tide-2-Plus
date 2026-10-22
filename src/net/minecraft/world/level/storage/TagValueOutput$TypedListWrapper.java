package net.minecraft.world.level.storage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.DataResult.Error;
import com.mojang.serialization.DataResult.Success;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.ProblemReporter;

class TagValueOutput$TypedListWrapper<T> implements ValueOutput$TypedOutputList<T> {
   private final ProblemReporter problemReporter;
   private final String name;
   private final DynamicOps<Tag> ops;
   private final Codec<T> codec;
   private final ListTag output;

   private TagValueOutput$TypedListWrapper(
      final ProblemReporter problemReporter, final String name, final DynamicOps<Tag> ops, final Codec<T> codec, final ListTag output
   ) {
      this.problemReporter = problemReporter;
      this.name = name;
      this.ops = ops;
      this.codec = codec;
      this.output = output;
   }

   @Override
   public void add(final T value) {
      switch (this.codec.encodeStart(this.ops, value)) {
         case Success<Tag> success:
            this.output.add((Tag)success.value());
            break;
         case Error<Tag> error:
            this.problemReporter.report(new TagValueOutput$EncodeToListFailedProblem(this.name, value, error));
            error.partialValue().ifPresent(this.output::add);
            break;
         default:
            throw new MatchException(null, null);
      }
   }

   @Override
   public boolean isEmpty() {
      return this.output.isEmpty();
   }
}
