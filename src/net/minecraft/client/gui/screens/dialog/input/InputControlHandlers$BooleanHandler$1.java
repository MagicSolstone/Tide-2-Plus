package net.minecraft.client.gui.screens.dialog.input;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.dialog.action.Action$ValueGetter;
import net.minecraft.server.dialog.input.BooleanInput;

@Environment(EnvType.CLIENT)
class InputControlHandlers$BooleanHandler$1 implements Action$ValueGetter {
   InputControlHandlers$BooleanHandler$1(final InputControlHandlers$BooleanHandler this$0, final Checkbox val$control, final BooleanInput val$input) {
      this.val$control = val$controlx;
      this.val$input = val$inputx;
      Objects.requireNonNull(this$0);
      super();
   }

   @Override
   public String asTemplateSubstitution() {
      return this.val$control.selected() ? this.val$input.onTrue() : this.val$input.onFalse();
   }

   @Override
   public Tag asTag() {
      return ByteTag.valueOf(this.val$control.selected());
   }
}
