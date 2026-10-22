package net.minecraft.client.gui.screens.inventory;

import java.util.Objects;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.protocol.game.ServerboundSetBeaconPacket;

@Environment(EnvType.CLIENT)
class BeaconScreen$BeaconConfirmButton extends BeaconScreen$BeaconSpriteScreenButton {
   public BeaconScreen$BeaconConfirmButton(final BeaconScreen this$0, final int x, final int y) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(x, y, BeaconScreen.CONFIRM_SPRITE, CommonComponents.GUI_DONE);
   }

   @Override
   public void onPress(final InputWithModifiers input) {
      BeaconScreen.access$000(this.this$0)
         .getConnection()
         .send(new ServerboundSetBeaconPacket(Optional.ofNullable(this.this$0.primary), Optional.ofNullable(this.this$0.secondary)));
      BeaconScreen.access$100(this.this$0).player.closeContainer();
   }

   @Override
   public void updateStatus(final int levels) {
      this.active = this.this$0.menu.hasPayment() && this.this$0.primary != null;
   }
}
