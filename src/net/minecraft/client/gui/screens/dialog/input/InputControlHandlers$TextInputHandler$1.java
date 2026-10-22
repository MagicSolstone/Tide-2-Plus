package net.minecraft.client.gui.screens.dialog.input;

import java.util.Objects;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.dialog.action.Action$ValueGetter;

@Environment(EnvType.CLIENT)
class InputControlHandlers$TextInputHandler$1 implements Action$ValueGetter {
   InputControlHandlers$TextInputHandler$1(final InputControlHandlers$TextInputHandler this$0, final Supplier val$getter) {
      this.val$getter = val$getterx;
      Objects.requireNonNull(this$0);
      super();
   }

   @Override
   public String asTemplateSubstitution() {
      return StringTag.escapeWithoutQuotes((String)this.val$getter.get());
   }

   @Override
   public Tag asTag() {
      return StringTag.valueOf((String)this.val$getter.get());
   }
}
