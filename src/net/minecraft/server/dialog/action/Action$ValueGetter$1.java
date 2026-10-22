package net.minecraft.server.dialog.action;

import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

class Action$ValueGetter$1 implements Action$ValueGetter {
   Action$ValueGetter$1(final String val$value) {
      this.val$value = val$valuex;
   }

   @Override
   public String asTemplateSubstitution() {
      return this.val$value;
   }

   @Override
   public Tag asTag() {
      return StringTag.valueOf(this.val$value);
   }
}
