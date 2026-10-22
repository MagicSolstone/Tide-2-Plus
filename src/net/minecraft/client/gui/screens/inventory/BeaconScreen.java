package net.minecraft.client.gui.screens.inventory;

import com.google.common.collect.Lists;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.BeaconMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class BeaconScreen extends AbstractContainerScreen<BeaconMenu> {
   private static final Identifier BEACON_LOCATION = Identifier.withDefaultNamespace("textures/gui/container/beacon.png");
   private static final Identifier BUTTON_DISABLED_SPRITE = Identifier.withDefaultNamespace("container/beacon/button_disabled");
   private static final Identifier BUTTON_SELECTED_SPRITE = Identifier.withDefaultNamespace("container/beacon/button_selected");
   private static final Identifier BUTTON_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("container/beacon/button_highlighted");
   private static final Identifier BUTTON_SPRITE = Identifier.withDefaultNamespace("container/beacon/button");
   private static final Identifier CONFIRM_SPRITE = Identifier.withDefaultNamespace("container/beacon/confirm");
   private static final Identifier CANCEL_SPRITE = Identifier.withDefaultNamespace("container/beacon/cancel");
   private static final Component PRIMARY_EFFECT_LABEL = Component.translatable("block.minecraft.beacon.primary");
   private static final Component SECONDARY_EFFECT_LABEL = Component.translatable("block.minecraft.beacon.secondary");
   private final List<BeaconScreen$BeaconButton> beaconButtons = Lists.newArrayList();
   @Nullable
   private Holder<MobEffect> primary;
   @Nullable
   private Holder<MobEffect> secondary;

   public BeaconScreen(final BeaconMenu menu, final Inventory inventory, final Component title) {
      super(menu, inventory, title, 230, 219);
      menu.addSlotListener(new BeaconScreen$1(this, menu));
   }

   private <T extends AbstractWidget & BeaconScreen$BeaconButton> void addBeaconButton(final T beaconButton) {
      this.addRenderableWidget(beaconButton);
      this.beaconButtons.add(beaconButton);
   }

   @Override
   protected void init() {
      super.init();
      this.beaconButtons.clear();

      for (int tier = 0; tier <= 2; tier++) {
         int count = BeaconBlockEntity.BEACON_EFFECTS.get(tier).size();
         int totalWidth = count * 22 + (count - 1) * 2;

         for (int c = 0; c < count; c++) {
            Holder<MobEffect> effect = BeaconBlockEntity.BEACON_EFFECTS.get(tier).get(c);
            BeaconScreen$BeaconPowerButton beaconPowerButton = new BeaconScreen$BeaconPowerButton(
               (int)this, this.leftPos + 76 + c * 24 - totalWidth / 2, this.topPos + 22 + tier * 25, (boolean)effect, true, tier
            );
            beaconPowerButton.active = false;
            this.addBeaconButton(beaconPowerButton);
         }
      }

      int tier = 3;
      int count = BeaconBlockEntity.BEACON_EFFECTS.get(3).size() + 1;
      int totalWidth = count * 22 + (count - 1) * 2;

      for (int c = 0; c < count - 1; c++) {
         Holder<MobEffect> effect = BeaconBlockEntity.BEACON_EFFECTS.get(3).get(c);
         BeaconScreen$BeaconPowerButton beaconPowerButton = new BeaconScreen$BeaconPowerButton(
            (int)this, this.leftPos + 167 + c * 24 - totalWidth / 2, this.topPos + 47, (boolean)effect, false, 3
         );
         beaconPowerButton.active = false;
         this.addBeaconButton(beaconPowerButton);
      }

      Holder<MobEffect> dummyEffect = BeaconBlockEntity.BEACON_EFFECTS.get(0).get(0);
      BeaconScreen$BeaconPowerButton beaconPowerButton = new BeaconScreen$BeaconUpgradePowerButton(
         (int)this, this.leftPos + 167 + (count - 1) * 24 - totalWidth / 2, this.topPos + 47, dummyEffect
      );
      beaconPowerButton.visible = false;
      this.addBeaconButton(beaconPowerButton);
      this.addBeaconButton(new BeaconScreen$BeaconConfirmButton(this, this.leftPos + 164, this.topPos + 107));
      this.addBeaconButton(new BeaconScreen$BeaconCancelButton(this, this.leftPos + 190, this.topPos + 107));
   }

   @Override
   public void containerTick() {
      super.containerTick();
      this.updateButtons();
   }

   private void updateButtons() {
      int levels = this.menu.getLevels();
      this.beaconButtons.forEach(b -> b.updateStatus(levels));
   }

   @Override
   protected void extractLabels(final GuiGraphicsExtractor graphics, final int xm, final int ym) {
      graphics.centeredText(this.font, PRIMARY_EFFECT_LABEL, 62, 10, -2039584);
      graphics.centeredText(this.font, SECONDARY_EFFECT_LABEL, 169, 10, -2039584);
   }

   @Override
   public void extractBackground(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
      super.extractBackground(graphics, mouseX, mouseY, a);
      int xo = (this.width - this.imageWidth) / 2;
      int yo = (this.height - this.imageHeight) / 2;
      graphics.blit(RenderPipelines.GUI_TEXTURED, BEACON_LOCATION, xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
      graphics.item(new ItemStack(Items.NETHERITE_INGOT), xo + 20, yo + 109);
      graphics.item(new ItemStack(Items.EMERALD), xo + 41, yo + 109);
      graphics.item(new ItemStack(Items.DIAMOND), xo + 41 + 22, yo + 109);
      graphics.item(new ItemStack(Items.GOLD_INGOT), xo + 42 + 44, yo + 109);
      graphics.item(new ItemStack(Items.IRON_INGOT), xo + 42 + 66, yo + 109);
   }
}
