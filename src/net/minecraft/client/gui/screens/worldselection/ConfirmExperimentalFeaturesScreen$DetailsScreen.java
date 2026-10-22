package net.minecraft.client.gui.screens.worldselection;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
class ConfirmExperimentalFeaturesScreen$DetailsScreen extends Screen {
   private static final Component TITLE = Component.translatable("selectWorld.experimental.details.title");
   private final HeaderAndFooterLayout layout;
   private ConfirmExperimentalFeaturesScreen$DetailsScreen$PackList list;

   private ConfirmExperimentalFeaturesScreen$DetailsScreen(final ConfirmExperimentalFeaturesScreen this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(TITLE);
      this.layout = new HeaderAndFooterLayout(this);
   }

   @Override
   protected void init() {
      this.layout.addTitleHeader(TITLE, this.font);
      this.list = this.layout.addToContents(new ConfirmExperimentalFeaturesScreen$DetailsScreen$PackList(this, this.minecraft, this.this$0.enabledPacks));
      this.layout.addToFooter(Button.builder(CommonComponents.GUI_BACK, button -> this.onClose()).build());
      this.layout.visitWidgets(x$0 -> this.addRenderableWidget(x$0));
      this.repositionElements();
   }

   @Override
   protected void repositionElements() {
      if (this.list != null) {
         this.list.updateSize(this.width, this.layout);
      }

      this.layout.arrangeElements();
   }

   @Override
   public void onClose() {
      this.minecraft.gui.setScreen(this.this$0);
   }
}
