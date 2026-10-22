package com.mojang.realmsclient;

import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.gui.screens.RealmsCreateRealmScreen;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.PopupScreen;
import net.minecraft.client.gui.components.PopupScreen$Builder;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetTooltipHolder;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;

@Environment(EnvType.CLIENT)
class RealmsMainScreen$AvailableSnapshotEntry extends RealmsMainScreen$Entry {
   private static final Component START_SNAPSHOT_REALM = Component.translatable("mco.snapshot.start");
   private static final int TEXT_PADDING = 5;
   private final WidgetTooltipHolder tooltip;
   private final RealmsServer parent;

   public RealmsMainScreen$AvailableSnapshotEntry(final RealmsMainScreen this$0, final RealmsServer parent) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(this$0x);
      this.tooltip = new WidgetTooltipHolder();
      this.parent = parent;
      this.tooltip.set(Tooltip.create(Component.translatable("mco.snapshot.tooltip")));
   }

   @Override
   public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
      graphics.blitSprite(RenderPipelines.GUI_TEXTURED, RealmsMainScreen.NEW_REALM_SPRITE, this.getContentX() - 5, this.getContentYMiddle() - 10, 40, 20);
      int textYPos = this.getContentYMiddle() - 9 / 2;
      graphics.text(RealmsMainScreen.access$1900(this.this$0), START_SNAPSHOT_REALM, this.getContentX() + 40 - 2, textYPos - 5, -8388737);
      graphics.text(
         RealmsMainScreen.access$2000(this.this$0),
         Component.translatable("mco.snapshot.description", Objects.requireNonNullElse(this.parent.name, "unknown server")),
         this.getContentX() + 40 - 2,
         textYPos + 5,
         -8355712
      );
      this.tooltip
         .refreshTooltipForNextRenderPass(
            graphics,
            mouseX,
            mouseY,
            hovered,
            this.isFocused() && RealmsMainScreen.access$2100(this.this$0).gui.screen() == this.this$0,
            new ScreenRectangle(this.getContentX(), this.getContentY(), this.getContentWidth(), this.getContentHeight())
         );
   }

   @Override
   public boolean mouseClicked(final MouseButtonEvent event, final boolean doubleClick) {
      this.addSnapshotRealm();
      return true;
   }

   @Override
   public boolean keyPressed(final KeyEvent event) {
      if (event.isSelection()) {
         this.addSnapshotRealm();
         return false;
      } else {
         return super.keyPressed(event);
      }
   }

   private void addSnapshotRealm() {
      RealmsMainScreen.access$2200(this.this$0).getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
      RealmsMainScreen.access$2400(this.this$0)
         .gui
         .setScreen(
            new PopupScreen$Builder(this.this$0, Component.translatable("mco.snapshot.createSnapshotPopup.title"))
               .addMessage(Component.translatable("mco.snapshot.createSnapshotPopup.text"))
               .addButton(
                  Component.translatable("mco.selectServer.create"),
                  var1 -> RealmsMainScreen.access$2300(this.this$0).gui.setScreen(new RealmsCreateRealmScreen(this.this$0, this.parent, true))
               )
               .addButton(CommonComponents.GUI_CANCEL, PopupScreen::onClose)
               .build()
         );
   }

   @Override
   public Component getNarration() {
      return Component.translatable(
         "gui.narrate.button",
         CommonComponents.joinForNarration(
            START_SNAPSHOT_REALM, Component.translatable("mco.snapshot.description", Objects.requireNonNullElse(this.parent.name, "unknown server"))
         )
      );
   }
}
