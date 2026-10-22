package com.mojang.realmsclient.gui.screens.configuration;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
class RealmsSlotOptionsScreen$SettingsSlider extends AbstractSliderButton {
   private final double minValue;
   private final double maxValue;

   public RealmsSlotOptionsScreen$SettingsSlider(
      final RealmsSlotOptionsScreen this$0, final int x, final int y, final int width, final int currentValue, final float minValue, final float maxValue
   ) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(x, y, width, 20, CommonComponents.EMPTY, 0.0);
      this.minValue = minValue;
      this.maxValue = maxValue;
      this.value = (Mth.clamp((float)currentValue, minValue, maxValue) - minValue) / (maxValue - minValue);
      this.updateMessage();
   }

   @Override
   public void applyValue() {
      if (this.this$0.spawnProtectionButton.active) {
         this.this$0.spawnProtection = (int)Mth.lerp(Mth.clamp(this.value, 0.0, 1.0), this.minValue, this.maxValue);
      }
   }

   @Override
   protected void updateMessage() {
      this.setMessage(
         CommonComponents.optionNameValue(
            RealmsSlotOptionsScreen.SPAWN_PROTECTION_TEXT,
            (Component)(this.this$0.spawnProtection == 0 ? CommonComponents.OPTION_OFF : Component.literal(String.valueOf(this.this$0.spawnProtection)))
         )
      );
   }
}
