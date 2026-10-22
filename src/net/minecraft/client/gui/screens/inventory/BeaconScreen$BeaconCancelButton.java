package net.minecraft.client.gui.screens.inventory;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.network.chat.CommonComponents;

@Environment(EnvType.CLIENT)
class BeaconScreen$BeaconCancelButton extends BeaconScreen$BeaconSpriteScreenButton {
   public BeaconScreen$BeaconCancelButton(final BeaconScreen this$0, final int x, final int y) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(x, y, BeaconScreen.CANCEL_SPRITE, CommonComponents.GUI_CANCEL);
   }

   @Override
   public void onPress(final InputWithModifiers input) {
      BeaconScreen.access$200(this.this$0).player.closeContainer();
   }

   @Override
   public void updateStatus(final int levels) {
   }
}
