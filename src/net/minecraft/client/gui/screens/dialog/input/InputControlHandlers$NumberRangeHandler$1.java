package net.minecraft.client.gui.screens.dialog.input;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.dialog.action.Action$ValueGetter;

@Environment(EnvType.CLIENT)
class InputControlHandlers$NumberRangeHandler$1 implements Action$ValueGetter {
   InputControlHandlers$NumberRangeHandler$1(
      final InputControlHandlers$NumberRangeHandler this$0, final InputControlHandlers$NumberRangeHandler$SliderImpl val$control
   ) {
      this.val$control = val$controlx;
      Objects.requireNonNull(this$0);
      super();
   }

   @Override
   public String asTemplateSubstitution() {
      return this.val$control.stringValueToSend();
   }

   @Override
   public Tag asTag() {
      return FloatTag.valueOf(this.val$control.floatValueToSend());
   }
}
