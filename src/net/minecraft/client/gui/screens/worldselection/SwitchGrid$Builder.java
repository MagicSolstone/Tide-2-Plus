package net.minecraft.client.gui.screens.worldselection;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.SpacerElement;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class SwitchGrid$Builder {
   private final int width;
   private final List<SwitchGrid$SwitchBuilder> switchBuilders = new ArrayList<>();
   private int paddingLeft;
   private int rowSpacing = 4;
   private int rowCount;
   private Optional<SwitchGrid$InfoUnderneathSettings> infoUnderneath = Optional.empty();

   public SwitchGrid$Builder(final int width) {
      this.width = width;
   }

   private void increaseRow() {
      this.rowCount++;
   }

   public SwitchGrid$SwitchBuilder addSwitch(final Component label, final BooleanSupplier stateSupplier, final Consumer<Boolean> onClicked) {
      SwitchGrid$SwitchBuilder switchBuilder = new SwitchGrid$SwitchBuilder(label, stateSupplier, onClicked, 44);
      this.switchBuilders.add(switchBuilder);
      return switchBuilder;
   }

   public SwitchGrid$Builder withPaddingLeft(final int paddingLeft) {
      this.paddingLeft = paddingLeft;
      return this;
   }

   public SwitchGrid$Builder withRowSpacing(final int rowSpacing) {
      this.rowSpacing = rowSpacing;
      return this;
   }

   public SwitchGrid build() {
      GridLayout switchGrid = new GridLayout().rowSpacing(this.rowSpacing);
      switchGrid.addChild(SpacerElement.width(this.width - 44), 0, 0);
      switchGrid.addChild(SpacerElement.width(44), 0, 1);
      List<SwitchGrid$LabeledSwitch> switches = new ArrayList<>();
      this.rowCount = 0;

      for (SwitchGrid$SwitchBuilder switchBuilder : this.switchBuilders) {
         switches.add(switchBuilder.build(this, switchGrid, 0));
      }

      switchGrid.arrangeElements();
      SwitchGrid result = new SwitchGrid(switches, switchGrid);
      result.refreshStates();
      return result;
   }

   public SwitchGrid$Builder withInfoUnderneath(final int maxRows, final boolean alwaysMaxHeight) {
      this.infoUnderneath = Optional.of(new SwitchGrid$InfoUnderneathSettings(maxRows, alwaysMaxHeight));
      return this;
   }
}
