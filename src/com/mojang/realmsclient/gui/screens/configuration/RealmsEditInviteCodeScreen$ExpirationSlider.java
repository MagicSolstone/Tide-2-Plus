package com.mojang.realmsclient.gui.screens.configuration;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
class RealmsEditInviteCodeScreen$ExpirationSlider extends AbstractSliderButton {
   private final InviteCodeExpiration[] values;

   private RealmsEditInviteCodeScreen$ExpirationSlider(final RealmsEditInviteCodeScreen this$0, final int width) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(0, 0, width, 20, CommonComponents.EMPTY, (double)this$0x.expiration.ordinal() / (InviteCodeExpiration.values().length - 1));
      this.values = InviteCodeExpiration.values();
      this.updateMessage();
   }

   @Override
   protected void updateMessage() {
      this.setMessage(CommonComponents.optionNameValue(RealmsEditInviteCodeScreen.EXPIRATION_DATE_LABEL, this.this$0.expiration.getLabel()));
   }

   @Override
   protected void applyValue() {
      int index = Mth.clamp((int)Math.round(this.value * (this.values.length - 1)), 0, this.values.length - 1);
      InviteCodeExpiration selectedExpiration = this.values[index];
      this.value = this.sliderValue(index);
      if (selectedExpiration != this.this$0.expiration) {
         this.this$0.expiration = selectedExpiration;
         this.this$0.expirationChanged = true;
      }
   }

   @Override
   public boolean keyPressed(final KeyEvent event) {
      if (this.canChangeValue && (event.isLeft() || event.isRight())) {
         int direction = event.isLeft() ? -1 : 1;
         int index = Mth.clamp(this.this$0.expiration.ordinal() + direction, 0, this.values.length - 1);
         this.setValue(this.sliderValue(index));
         return true;
      } else {
         return super.keyPressed(event);
      }
   }

   private double sliderValue(final int index) {
      return (double)index / (this.values.length - 1);
   }
}
