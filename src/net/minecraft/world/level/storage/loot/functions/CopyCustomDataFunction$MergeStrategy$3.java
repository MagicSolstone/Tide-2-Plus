package net.minecraft.world.level.storage.loot.functions;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.List;
import net.minecraft.commands.arguments.NbtPathArgument$NbtPath;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

enum CopyCustomDataFunction$MergeStrategy$3 {
   private CopyCustomDataFunction$MergeStrategy$3(final String name) {
   }

   @Override
   public void merge(final Tag target, final NbtPathArgument$NbtPath path, final List<Tag> sources) throws CommandSyntaxException {
      List<Tag> targets = path.getOrCreate(target, CompoundTag::new);
      targets.forEach(tag -> {
         if (tag instanceof CompoundTag compoundTag) {
            sources.forEach(source -> {
               if (source instanceof CompoundTag sourceTag) {
                  compoundTag.merge(sourceTag);
               }
            });
         }
      });
   }
}
