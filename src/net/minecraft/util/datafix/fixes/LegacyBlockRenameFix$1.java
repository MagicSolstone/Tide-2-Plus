package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.schemas.Schema;
import java.util.function.Function;

class LegacyBlockRenameFix$1 extends LegacyBlockRenameFix {
   LegacyBlockRenameFix$1(final Schema outputSchema, final String name, final Function val$renamer) {
      super(outputSchema, name);
      this.val$renamer = val$renamerx;
   }

   @Override
   protected String renameBlock(final String block) {
      return (String)this.val$renamer.apply(block);
   }
}
