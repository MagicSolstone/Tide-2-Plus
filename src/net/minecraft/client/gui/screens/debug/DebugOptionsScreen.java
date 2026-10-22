package net.minecraft.client.gui.screens.debug;

import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.debug.DebugScreenProfile;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.layouts.SpacerElement;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class DebugOptionsScreen extends Screen {
   private static final Component TITLE = Component.translatable("debug.options.title");
   private static final Component SUBTITLE = Component.translatable("debug.options.warning").withColor(-2142128);
   private static final Component ENABLED_TEXT = Component.translatable("debug.entry.always");
   private static final Component IN_OVERLAY_TEXT = Component.translatable("debug.entry.overlay");
   private static final Component DISABLED_TEXT = CommonComponents.OPTION_OFF;
   private static final Component NOT_ALLOWED_TOOLTIP = Component.translatable("debug.options.notAllowed.tooltip");
   private static final Component SEARCH = Component.translatable("debug.options.search").withStyle(EditBox.SEARCH_HINT_STYLE);
   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this, 61, 33);
   private DebugOptionsScreen$OptionList optionList;
   private EditBox searchBox;
   private final List<Button> profileButtons = new ArrayList<>();

   public DebugOptionsScreen() {
      super(TITLE);
   }

   @Override
   protected void init() {
      LinearLayout header = this.layout.addToHeader(LinearLayout.vertical().spacing(8));
      this.optionList = new DebugOptionsScreen$OptionList(this);
      int optionListWidth = this.optionList.getRowWidth();
      LinearLayout title = LinearLayout.horizontal().spacing(8);
      title.addChild(new SpacerElement(optionListWidth / 3, 1));
      title.addChild(new StringWidget(TITLE, this.font), title.newCellSettings().alignVerticallyMiddle());
      this.searchBox = new EditBox(this.font, 0, 0, optionListWidth / 3, 20, this.searchBox, SEARCH);
      this.searchBox.setResponder(value -> this.optionList.updateSearch(value));
      this.searchBox.setHint(SEARCH);
      title.addChild(this.searchBox);
      header.addChild(title, LayoutSettings::alignHorizontallyCenter);
      header.addChild(new MultiLineTextWidget(SUBTITLE, this.font).setMaxWidth(optionListWidth).setCentered(true), LayoutSettings::alignHorizontallyCenter);
      this.layout.addToContents(this.optionList);
      LinearLayout bottomButtons = this.layout.addToFooter(LinearLayout.horizontal().spacing(4));
      this.addProfileButton(DebugScreenProfile.DEFAULT, bottomButtons);
      this.addProfileButton(DebugScreenProfile.PERFORMANCE, bottomButtons);
      AbstractWidget guiScaleButton = this.minecraft.options.debugGuiScale().createButton(this.minecraft.options);
      guiScaleButton.setWidth(110);
      bottomButtons.addChild(guiScaleButton);
      bottomButtons.addChild(Button.builder(CommonComponents.GUI_DONE, button -> this.onClose()).width(60).build());
      this.layout.visitWidgets(x$0 -> this.addRenderableWidget(x$0));
      this.repositionElements();
   }

   @Override
   public void extractBlurredBackground(final GuiGraphicsExtractor graphics) {
      this.minecraft.gui.hud.extractDebugOverlay(graphics);
      super.extractBlurredBackground(graphics);
   }

   @Override
   protected void setInitialFocus() {
      this.setInitialFocus(this.searchBox);
   }

   private void addProfileButton(final DebugScreenProfile profile, final LinearLayout bottomButtons) {
      Button profileButton = Button.builder(Component.translatable(profile.translationKey()), button -> {
         this.minecraft.debugEntries.loadProfile(profile);
         this.minecraft.debugEntries.save();
         this.optionList.refreshEntries();

         for (Button listButton : this.profileButtons) {
            listButton.active = true;
         }

         button.active = false;
      }).width(120).build();
      profileButton.active = !this.minecraft.debugEntries.isUsingProfile(profile);
      this.profileButtons.add(profileButton);
      bottomButtons.addChild(profileButton);
   }

   @Override
   protected void repositionElements() {
      this.layout.arrangeElements();
      if (this.optionList != null) {
         this.optionList.updateSize(this.width, this.layout);
      }
   }

   public DebugOptionsScreen$OptionList getOptionList() {
      return this.optionList;
   }
}
