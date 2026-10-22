package net.minecraft.world.level.storage.loot.functions;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.List;
import net.minecraft.commands.arguments.NbtPathArgument$NbtPath;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

enum CopyCustomDataFunction$MergeStrategy$2 {
   private CopyCustomDataFunction$MergeStrategy$2(final String name) {
   }

   @Override
   public void merge(final Tag target, final NbtPathArgument$NbtPath path, final List<Tag> sources) throws CommandSyntaxException {
      List<Tag> targets = path.getOrCreate(target, ListTag::new);
      targets.forEach(tag -> {
         if (tag instanceof ListTag listTag) {
            sources.forEach(source -> listTag.add(source.copy()));
         }
      });
   }
}
