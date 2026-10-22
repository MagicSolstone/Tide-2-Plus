package net.minecraft.client.gui.screens.inventory;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;

@Environment(EnvType.CLIENT)
class BeaconScreen$BeaconPowerButton extends BeaconScreen$BeaconScreenButton {
   private final boolean isPrimary;
   protected final int tier;
   private Holder<MobEffect> effect;
   private Identifier sprite;

   public BeaconScreen$BeaconPowerButton(final int this$0, final int x, final Holder<MobEffect> y, final boolean effect, final int isPrimary, final int tier) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(x, y);
      this.isPrimary = isPrimary;
      this.tier = tier;
      this.setEffect(effect);
   }

   protected void setEffect(final Holder<MobEffect> effect) {
      this.effect = effect;
      this.sprite = Hud.getMobEffectSprite(effect);
      this.setTooltip(Tooltip.create(this.createEffectDescription(effect), null));
   }

   protected MutableComponent createEffectDescription(final Holder<MobEffect> effect) {
      return Component.translatable(effect.value().getDescriptionId());
   }

   @Override
   public void onPress(final InputWithModifiers input) {
      if (!this.isSelected()) {
         if (this.isPrimary) {
            this.this$0.primary = this.effect;
            if (!Objects.equals(this.this$0.secondary, this.effect)) {
               this.this$0.secondary = null;
            }
         } else {
            this.this$0.secondary = this.effect;
         }

         this.this$0.updateButtons();
      }
   }

   @Override
   protected void extractIcon(final GuiGraphicsExtractor graphics) {
      graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.sprite, this.getX() + 2, this.getY() + 2, 18, 18);
   }

   @Override
   public void updateStatus(final int levels) {
      this.active = this.tier < levels;
      this.setSelected(this.effect.equals(this.isPrimary ? this.this$0.primary : this.this$0.secondary));
   }

   @Override
   protected MutableComponent createNarrationMessage() {
      return this.createEffectDescription(this.effect);
   }
}
