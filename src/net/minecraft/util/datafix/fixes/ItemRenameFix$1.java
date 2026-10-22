package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.schemas.Schema;
import java.util.function.Function;

class ItemRenameFix$1 extends ItemRenameFix {
   ItemRenameFix$1(final Schema outputSchema, final String name, final Function val$fixItem) {
      super(outputSchema, name);
      this.val$fixItem = val$fixItemx;
   }

   @Override
   protected String fixItem(final String item) {
      return (String)this.val$fixItem.apply(item);
   }
}
