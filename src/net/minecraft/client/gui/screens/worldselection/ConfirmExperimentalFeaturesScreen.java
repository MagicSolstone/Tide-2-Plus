package net.minecraft.client.gui.screens.worldselection;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import java.util.Collection;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.GridLayout$RowHelper;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.Pack;

@Environment(EnvType.CLIENT)
public class ConfirmExperimentalFeaturesScreen extends Screen {
   private static final Component TITLE = Component.translatable("selectWorld.experimental.title");
   private static final Component MESSAGE = Component.translatable("selectWorld.experimental.message");
   private static final Component DETAILS_BUTTON = Component.translatable("selectWorld.experimental.details");
   private static final int COLUMN_SPACING = 10;
   private static final int DETAILS_BUTTON_WIDTH = 100;
   private final BooleanConsumer callback;
   private final Collection<Pack> enabledPacks;
   private final GridLayout layout = new GridLayout().columnSpacing(10).rowSpacing(20);

   public ConfirmExperimentalFeaturesScreen(final Collection<Pack> enabledPacks, final BooleanConsumer callback) {
      super(TITLE);
      this.enabledPacks = enabledPacks;
      this.callback = callback;
   }

   @Override
   public Component getNarrationMessage() {
      return CommonComponents.joinForNarration(super.getNarrationMessage(), MESSAGE);
   }

   @Override
   protected void init() {
      super.init();
      GridLayout$RowHelper helper = this.layout.createRowHelper(2);
      LayoutSettings centered = helper.newCellSettings().alignHorizontallyCenter();
      helper.addChild(new StringWidget(this.title, this.font), 2, centered);
      MultiLineTextWidget messageLabel = helper.addChild(new MultiLineTextWidget(MESSAGE, this.font).setCentered(true), 2, centered);
      messageLabel.setMaxWidth(310);
      helper.addChild(
         Button.builder(DETAILS_BUTTON, var1x -> this.minecraft.gui.setScreen(new ConfirmExperimentalFeaturesScreen$DetailsScreen(this))).width(100).build(),
         2,
         centered
      );
      helper.addChild(Button.builder(CommonComponents.GUI_PROCEED, button -> this.callback.accept(true)).build());
      helper.addChild(Button.builder(CommonComponents.GUI_BACK, button -> this.callback.accept(false)).build());
      this.layout.visitWidgets(x$0 -> this.addRenderableWidget(x$0));
      this.layout.arrangeElements();
      this.repositionElements();
   }

   @Override
   protected void repositionElements() {
      FrameLayout.alignInRectangle(this.layout, 0, 0, this.width, this.height, 0.5F, 0.5F);
   }

   @Override
   public void onClose() {
      this.callback.accept(false);
   }
}
