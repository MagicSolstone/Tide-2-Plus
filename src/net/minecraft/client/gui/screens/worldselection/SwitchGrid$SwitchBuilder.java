package net.minecraft.client.gui.screens.worldselection;

import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.CycleButton$Builder;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class SwitchGrid$SwitchBuilder {
   private final Component label;
   private final BooleanSupplier stateSupplier;
   private final Consumer<Boolean> onClicked;
   @Nullable
   private Component info;
   @Nullable
   private BooleanSupplier isActiveCondition;
   private final int buttonWidth;

   private SwitchGrid$SwitchBuilder(final Component label, final BooleanSupplier stateSupplier, final Consumer<Boolean> onClicked, final int buttonWidth) {
      this.label = label;
      this.stateSupplier = stateSupplier;
      this.onClicked = onClicked;
      this.buttonWidth = buttonWidth;
   }

   public SwitchGrid$SwitchBuilder withIsActiveCondition(final BooleanSupplier isActiveCondition) {
      this.isActiveCondition = isActiveCondition;
      return this;
   }

   public SwitchGrid$SwitchBuilder withInfo(final Component info) {
      this.info = info;
      return this;
   }

   private SwitchGrid$LabeledSwitch build(final SwitchGrid$Builder switchGridBuilder, final GridLayout gridLayout, final int startColumn) {
      switchGridBuilder.increaseRow();
      StringWidget labelWidget = new StringWidget(this.label, Minecraft.getInstance().font);
      gridLayout.addChild(
         labelWidget, switchGridBuilder.rowCount, startColumn, gridLayout.newCellSettings().align(0.0F, 0.5F).paddingLeft(switchGridBuilder.paddingLeft)
      );
      Optional<SwitchGrid$InfoUnderneathSettings> infoUnderneath = switchGridBuilder.infoUnderneath;
      CycleButton$Builder<Boolean> buttonBuilder = CycleButton.onOffBuilder(this.stateSupplier.getAsBoolean());
      buttonBuilder.displayOnlyValue();
      boolean hasTooltip = this.info != null && infoUnderneath.isEmpty();
      if (hasTooltip) {
         Tooltip tooltip = Tooltip.create(this.info);
         buttonBuilder.withTooltip(value -> tooltip);
      }

      if (this.info != null && !hasTooltip) {
         buttonBuilder.withCustomNarration(buttonx -> CommonComponents.joinForNarration(this.label, buttonx.createDefaultNarrationMessage(), this.info));
      } else {
         buttonBuilder.withCustomNarration(buttonx -> CommonComponents.joinForNarration(this.label, buttonx.createDefaultNarrationMessage()));
      }

      CycleButton<Boolean> button = buttonBuilder.create(0, 0, this.buttonWidth, 20, Component.empty(), (b, value) -> this.onClicked.accept(value));
      if (this.isActiveCondition != null) {
         button.active = this.isActiveCondition.getAsBoolean();
      }

      gridLayout.addChild(button, switchGridBuilder.rowCount, startColumn + 1, gridLayout.newCellSettings().alignHorizontallyRight());
      if (this.info != null) {
         infoUnderneath.ifPresent(
            infoUnderneathSettings -> {
               Component styledInfo = this.info.copy().withStyle(ChatFormatting.GRAY);
               Font font = Minecraft.getInstance().font;
               MultiLineTextWidget infoWidget = new MultiLineTextWidget(styledInfo, font);
               infoWidget.setMaxWidth(switchGridBuilder.width - switchGridBuilder.paddingLeft - this.buttonWidth);
               infoWidget.setMaxRows(infoUnderneathSettings.maxInfoRows());
               switchGridBuilder.increaseRow();
               int extraBottomPadding = infoUnderneathSettings.alwaysMaxHeight ? 9 * infoUnderneathSettings.maxInfoRows - infoWidget.getHeight() : 0;
               gridLayout.addChild(
                  infoWidget,
                  switchGridBuilder.rowCount,
                  startColumn,
                  gridLayout.newCellSettings().paddingTop(-switchGridBuilder.rowSpacing).paddingBottom(extraBottomPadding)
               );
            }
         );
      }

      return new SwitchGrid$LabeledSwitch(button, this.stateSupplier, this.isActiveCondition);
   }
}
