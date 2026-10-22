package net.minecraft.world.level.storage.loot.functions;

import com.google.common.collect.Lists;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.List;
import net.minecraft.commands.arguments.NbtPathArgument$NbtPath;
import net.minecraft.world.level.storage.loot.providers.nbt.NbtProvider;

public class CopyCustomDataFunction$Builder extends LootItemConditionalFunction$Builder<CopyCustomDataFunction$Builder> {
   private final NbtProvider source;
   private final List<CopyCustomDataFunction$CopyOperation> ops = Lists.newArrayList();

   private CopyCustomDataFunction$Builder(final NbtProvider source) {
      this.source = source;
   }

   public CopyCustomDataFunction$Builder copy(final String sourcePath, final String targetPath, final CopyCustomDataFunction$MergeStrategy mergeStrategy) {
      try {
         this.ops.add(new CopyCustomDataFunction$CopyOperation(NbtPathArgument$NbtPath.of(sourcePath), NbtPathArgument$NbtPath.of(targetPath), mergeStrategy));
         return this;
      } catch (CommandSyntaxException var5) {
         throw new IllegalArgumentException(var5);
      }
   }

   public CopyCustomDataFunction$Builder copy(final String sourcePath, final String targetPath) {
      return this.copy(sourcePath, targetPath, CopyCustomDataFunction$MergeStrategy.REPLACE);
   }

   protected CopyCustomDataFunction$Builder getThis() {
      return this;
   }

   @Override
   public LootItemFunction build() {
      return new CopyCustomDataFunction(this.getCondition(), this.source, this.ops);
   }
}
