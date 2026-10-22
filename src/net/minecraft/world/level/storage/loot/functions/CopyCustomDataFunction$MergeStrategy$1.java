package net.minecraft.world.level.storage.loot.functions;

import com.google.common.collect.Iterables;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.List;
import net.minecraft.commands.arguments.NbtPathArgument$NbtPath;
import net.minecraft.nbt.Tag;

enum CopyCustomDataFunction$MergeStrategy$1 {
   private CopyCustomDataFunction$MergeStrategy$1(final String name) {
   }

   @Override
   public void merge(final Tag target, final NbtPathArgument$NbtPath path, final List<Tag> sources) throws CommandSyntaxException {
      path.set(target, (Tag)Iterables.getLast(sources));
   }
}
