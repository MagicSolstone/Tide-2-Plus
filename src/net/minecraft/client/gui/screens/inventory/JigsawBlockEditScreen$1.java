package net.minecraft.client.gui.screens.inventory;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
class JigsawBlockEditScreen$1 extends AbstractSliderButton {
   JigsawBlockEditScreen$1(
      final JigsawBlockEditScreen this$0, final int x, final int y, final int width, final int height, final Component message, final double initialValue
   ) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(x, y, width, height, message, initialValue);
      this.updateMessage();
   }

   @Override
   protected void updateMessage() {
      this.setMessage(Component.translatable("jigsaw_block.levels", this.this$0.levels));
   }

   @Override
   protected void applyValue() {
      this.this$0.levels = Mth.floor(Mth.clampedLerp(this.value, 0.0, 20.0));
   }
}
