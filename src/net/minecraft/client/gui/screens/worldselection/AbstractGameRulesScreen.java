package net.minecraft.client.gui.screens.worldselection;

import com.google.common.collect.Sets;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.gamerules.GameRules;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public abstract class AbstractGameRulesScreen extends Screen {
   protected static final Component TITLE = Component.translatable("editGamerule.title");
   private static final Component SEARCH_HINT = Component.translatable("gui.game_rule.search").withStyle(EditBox.SEARCH_HINT_STYLE);
   private static final int SEARCH_BOX_HEIGHT = 15;
   private final Set<AbstractGameRulesScreen$RuleEntry> invalidEntries = Sets.newHashSet();
   private final Consumer<Optional<GameRules>> exitCallback;
   protected final HeaderAndFooterLayout layout;
   protected final GameRules gameRules;
   @Nullable
   protected EditBox searchBox;
   protected AbstractGameRulesScreen$RuleList ruleList;
   @Nullable
   protected Button doneButton;

   public AbstractGameRulesScreen(final GameRules gameRules, final Consumer<Optional<GameRules>> exitCallback) {
      super(TITLE);
      this.gameRules = gameRules;
      this.exitCallback = exitCallback;
      this.layout = new HeaderAndFooterLayout(this, (int)(12.0 + 9.0 + 15.0), 33);
   }

   protected void createAndConfigureSearchBox(final LinearLayout headerLayout) {
      this.searchBox = headerLayout.addChild(new EditBox(this.font, 200, 15, Component.empty()));
      this.searchBox.setHint(SEARCH_HINT);
      this.searchBox.setResponder(this::filterGameRules);
   }

   @Override
   protected void init() {
      LinearLayout header = this.layout.addToHeader(LinearLayout.vertical().spacing(4));
      header.defaultCellSetting().alignHorizontallyCenter();
      header.addChild(new StringWidget(TITLE, this.font));
      this.createAndConfigureSearchBox(header);
      this.initContent();
      LinearLayout footer = this.layout.addToFooter(LinearLayout.horizontal().spacing(8));
      this.doneButton = footer.addChild(Button.builder(CommonComponents.GUI_DONE, button -> this.onDone()).build());
      footer.addChild(Button.builder(CommonComponents.GUI_CANCEL, button -> this.onClose()).build());
      this.layout.visitWidgets(x$0 -> this.addRenderableWidget(x$0));
      this.repositionElements();
   }

   protected abstract void initContent();

   protected abstract void onDone();

   @Override
   protected void repositionElements() {
      this.layout.arrangeElements();
      if (this.ruleList != null) {
         this.ruleList.updateSize(this.width, this.layout);
      }
   }

   @Override
   protected void setInitialFocus() {
      if (this.searchBox != null) {
         this.setInitialFocus(this.searchBox);
      }
   }

   private void markInvalid(final AbstractGameRulesScreen$RuleEntry invalidEntry) {
      this.invalidEntries.add(invalidEntry);
      this.updateDoneButton();
   }

   private void clearInvalid(final AbstractGameRulesScreen$RuleEntry invalidEntry) {
      this.invalidEntries.remove(invalidEntry);
      this.updateDoneButton();
   }

   private void updateDoneButton() {
      if (this.doneButton != null) {
         this.doneButton.active = this.invalidEntries.isEmpty();
      }
   }

   protected void closeAndDiscardChanges() {
      this.exitCallback.accept(Optional.empty());
   }

   protected void closeAndApplyChanges() {
      this.exitCallback.accept(Optional.of(this.gameRules));
   }

   protected void filterGameRules(final String filter) {
      if (this.ruleList != null) {
         this.ruleList.populateChildren(filter);
         this.ruleList.setScrollAmount(0.0);
         this.repositionElements();
      }
   }
}
