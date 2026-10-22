package net.minecraft.client.gui.screens.worldselection;

import java.util.Collection;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.Style;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.world.flag.FeatureFlags;

@Environment(EnvType.CLIENT)
class ConfirmExperimentalFeaturesScreen$DetailsScreen$PackList extends ObjectSelectionList<ConfirmExperimentalFeaturesScreen$DetailsScreen$PackListEntry> {
   public ConfirmExperimentalFeaturesScreen$DetailsScreen$PackList(final Minecraft this$1, final Collection<Pack> minecraft, final Collection selectedPacks) {
      Objects.requireNonNull(this$1);
      super(minecraft, this$1.width, this$1.layout.getContentHeight(), this$1.layout.getHeaderHeight(), (9 + 2) * 3);

      for (Pack pack : selectedPacks) {
         String nonVanillaFeatures = FeatureFlags.printMissingFlags(FeatureFlags.VANILLA_SET, pack.getRequestedFeatures());
         if (!nonVanillaFeatures.isEmpty()) {
            Component title = ComponentUtils.mergeStyles(pack.getTitle(), Style.EMPTY.withBold(true));
            Component message = Component.translatable("selectWorld.experimental.details.entry", nonVanillaFeatures);
            this.addEntry(
               new ConfirmExperimentalFeaturesScreen$DetailsScreen$PackListEntry(
                  this$1,
                  title,
                  message,
                  MultiLineLabel.create(ConfirmExperimentalFeaturesScreen$DetailsScreen.access$000(this$1), message, this.getRowWidth())
               )
            );
         }
      }
   }

   @Override
   public int getRowWidth() {
      return this.width * 3 / 4;
   }
}
