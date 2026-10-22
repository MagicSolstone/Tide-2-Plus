package com.mojang.realmsclient;

import com.mojang.realmsclient.dto.RealmsNotification$VisitUrl;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageWidget;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.SpacerElement;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class RealmsMainScreen$NotificationMessageEntry extends RealmsMainScreen$Entry {
   private static final int SIDE_MARGINS = 40;
   public static final int PADDING = 7;
   public static final int HEIGHT_WITHOUT_TEXT = 38;
   private final Component text;
   private final List<AbstractWidget> children;
   @Nullable
   private final RealmsMainScreen$CrossButton dismissButton;
   private final MultiLineTextWidget textWidget;
   private final GridLayout gridLayout;
   private final FrameLayout textFrame;
   private final Button button;
   private int lastEntryWidth;

   public RealmsMainScreen$NotificationMessageEntry(
      final RealmsMainScreen this$0,
      final RealmsMainScreen realmsMainScreen,
      final int messageHeight,
      final Component text,
      final RealmsNotification$VisitUrl notification
   ) {
      Objects.requireNonNull(this$0);
      super(this$0);
      this.children = new ArrayList<>();
      this.lastEntryWidth = -1;
      this.text = text;
      this.gridLayout = new GridLayout();
      this.gridLayout.addChild(ImageWidget.sprite(20, 20, RealmsMainScreen.INFO_SPRITE), 0, 0, this.gridLayout.newCellSettings().padding(7, 7, 0, 0));
      this.gridLayout.addChild(SpacerElement.width(40), 0, 0);
      this.textFrame = this.gridLayout.addChild(new FrameLayout(0, messageHeight), 0, 1, this.gridLayout.newCellSettings().paddingTop(7));
      this.textWidget = this.textFrame
         .addChild(
            new MultiLineTextWidget(text, RealmsMainScreen.access$1700(this$0)).setCentered(true),
            this.textFrame.newChildLayoutSettings().alignHorizontallyCenter().alignVerticallyTop()
         );
      this.gridLayout.addChild(SpacerElement.width(40), 0, 2);
      if (notification.dismissable()) {
         this.dismissButton = this.gridLayout
            .addChild(
               new RealmsMainScreen$CrossButton(b -> this$0.dismissNotification(notification.uuid()), Component.translatable("mco.notification.dismiss")),
               0,
               2,
               this.gridLayout.newCellSettings().alignHorizontallyRight().padding(0, 7, 7, 0)
            );
      } else {
         this.dismissButton = null;
      }

      this.button = this.gridLayout
         .addChild(notification.buildOpenLinkButton(realmsMainScreen), 1, 1, this.gridLayout.newCellSettings().alignHorizontallyCenter().padding(4));
      this.button.setOverrideRenderHighlightedSprite(() -> this.isFocused());
      this.gridLayout.visitWidgets(this.children::add);
   }

   @Override
   public boolean keyPressed(final KeyEvent event) {
      if (this.button.keyPressed(event)) {
         return true;
      } else {
         return this.dismissButton != null && this.dismissButton.keyPressed(event) ? true : super.keyPressed(event);
      }
   }

   private void updateEntryWidth() {
      int entryWidth = this.getWidth();
      if (this.lastEntryWidth != entryWidth) {
         this.refreshLayout(entryWidth);
         this.lastEntryWidth = entryWidth;
      }
   }

   private void refreshLayout(final int entryWidth) {
      int width = textWidth(entryWidth);
      this.textFrame.setMinWidth(width);
      this.textWidget.setMaxWidth(width);
      this.gridLayout.arrangeElements();
   }

   public static int textWidth(final int rowWidth) {
      return rowWidth - 80;
   }

   @Override
   public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
      this.gridLayout.setPosition(this.getContentX(), this.getContentY());
      this.updateEntryWidth();
      this.children.forEach(child -> child.extractRenderState(graphics, mouseX, mouseY, a));
   }

   @Override
   public boolean mouseClicked(final MouseButtonEvent event, final boolean doubleClick) {
      if (this.dismissButton != null && this.dismissButton.mouseClicked(event, doubleClick)) {
         return true;
      } else {
         return this.button.mouseClicked(event, doubleClick) ? true : super.mouseClicked(event, doubleClick);
      }
   }

   public Component getText() {
      return this.text;
   }

   @Override
   public Component getNarration() {
      return this.getText();
   }
}
