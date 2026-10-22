package net.minecraft.world.level.storage.loot.functions;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import java.util.List;
import net.minecraft.commands.arguments.NbtPathArgument$NbtPath;
import net.minecraft.nbt.Tag;
import net.minecraft.util.StringRepresentable;

public enum CopyCustomDataFunction$MergeStrategy implements StringRepresentable {
   REPLACE("replace"),
   APPEND("append"),
   MERGE("merge");

   public static final Codec<CopyCustomDataFunction$MergeStrategy> CODEC = StringRepresentable.fromEnum(CopyCustomDataFunction$MergeStrategy::values);
   private final String name;

   public abstract void merge(final Tag target, final NbtPathArgument$NbtPath path, List<Tag> sources) throws CommandSyntaxException;

   private CopyCustomDataFunction$MergeStrategy(final String name) {
      this.name = name;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }
}
