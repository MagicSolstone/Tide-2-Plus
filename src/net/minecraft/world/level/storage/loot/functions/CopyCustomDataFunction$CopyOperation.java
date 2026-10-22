package net.minecraft.world.level.storage.loot.functions;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.commands.arguments.NbtPathArgument$NbtPath;
import net.minecraft.nbt.Tag;

record CopyCustomDataFunction$CopyOperation(NbtPathArgument$NbtPath sourcePath, NbtPathArgument$NbtPath targetPath, CopyCustomDataFunction$MergeStrategy op) {
   public static final Codec<CopyCustomDataFunction$CopyOperation> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            NbtPathArgument$NbtPath.CODEC.fieldOf("source").forGetter(CopyCustomDataFunction$CopyOperation::sourcePath),
            NbtPathArgument$NbtPath.CODEC.fieldOf("target").forGetter(CopyCustomDataFunction$CopyOperation::targetPath),
            CopyCustomDataFunction$MergeStrategy.CODEC.fieldOf("op").forGetter(CopyCustomDataFunction$CopyOperation::op)
         )
         .apply(i, CopyCustomDataFunction$CopyOperation::new)
   );

   public void apply(final Supplier<Tag> target, final Tag source) {
      try {
         List<Tag> sourceTags = this.sourcePath.get(source);
         if (!sourceTags.isEmpty()) {
            this.op.merge(target.get(), this.targetPath, sourceTags);
         }
      } catch (CommandSyntaxException var4) {
      }
   }
}
