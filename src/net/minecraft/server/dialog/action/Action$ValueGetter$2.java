package net.minecraft.server.dialog.action;

import java.util.function.Supplier;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

class Action$ValueGetter$2 implements Action$ValueGetter {
   Action$ValueGetter$2(final Supplier val$value) {
      this.val$value = val$valuex;
   }

   @Override
   public String asTemplateSubstitution() {
      return (String)this.val$value.get();
   }

   @Override
   public Tag asTag() {
      return StringTag.valueOf((String)this.val$value.get());
   }
}
