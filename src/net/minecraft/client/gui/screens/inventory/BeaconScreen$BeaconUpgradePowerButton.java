package net.minecraft.client.gui.screens.inventory;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;

@Environment(EnvType.CLIENT)
class BeaconScreen$BeaconUpgradePowerButton extends BeaconScreen$BeaconPowerButton {
   public BeaconScreen$BeaconUpgradePowerButton(final int this$0, final int x, final Holder<MobEffect> y, final Holder effect) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super((int)this$0x, x, y, (boolean)effect, false, 3);
   }

   @Override
   protected MutableComponent createEffectDescription(final Holder<MobEffect> effect) {
      return Component.translatable(effect.value().getDescriptionId()).append(" II");
   }

   @Override
   public void updateStatus(final int levels) {
      if (this.this$0.primary != null) {
         this.visible = true;
         this.setEffect(this.this$0.primary);
         super.updateStatus(levels);
      } else {
         this.visible = false;
      }
   }
}
