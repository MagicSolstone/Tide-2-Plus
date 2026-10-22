package com.mojang.realmsclient.gui.screens.configuration;

import com.mojang.realmsclient.dto.RealmsRegion;
import com.mojang.realmsclient.dto.RegionSelectionPreference;
import com.mojang.realmsclient.dto.ServiceQuality;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class RealmsPreferredRegionSelectionScreen extends Screen {
   private static final Component REGION_SELECTION_LABEL = Component.translatable("mco.configure.world.region_preference.title");
   private static final int SPACING = 8;
   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
   private final Screen parent;
   private final BiConsumer<RegionSelectionPreference, RealmsRegion> applySettings;
   private final Map<RealmsRegion, ServiceQuality> regionServiceQuality;
   private RealmsPreferredRegionSelectionScreen$RegionSelectionList list;
   private RealmsSettingsTab$RegionSelection selection;
   @Nullable
   private Button doneButton;

   public RealmsPreferredRegionSelectionScreen(
      final Screen parent,
      final BiConsumer<RegionSelectionPreference, RealmsRegion> applySettings,
      final Map<RealmsRegion, ServiceQuality> regionServiceQuality,
      final RealmsSettingsTab$RegionSelection currentSelection
   ) {
      super(REGION_SELECTION_LABEL);
      this.parent = parent;
      this.applySettings = applySettings;
      this.regionServiceQuality = regionServiceQuality;
      this.selection = currentSelection;
   }

   @Override
   public void onClose() {
      this.minecraft.gui.setScreen(this.parent);
   }

   @Override
   protected void init() {
      LinearLayout header = this.layout.addToHeader(LinearLayout.vertical().spacing(8));
      header.defaultCellSetting().alignHorizontallyCenter();
      header.addChild(new StringWidget(this.getTitle(), this.font));
      this.list = this.layout.addToContents(new RealmsPreferredRegionSelectionScreen$RegionSelectionList(this));
      LinearLayout footer = this.layout.addToFooter(LinearLayout.horizontal().spacing(8));
      this.doneButton = footer.addChild(Button.builder(CommonComponents.GUI_DONE, button -> this.onDone()).build());
      footer.addChild(Button.builder(CommonComponents.GUI_CANCEL, button -> this.onClose()).build());
      this.list.setSelected(this.list.children().stream().filter(e -> Objects.equals(e.regionSelection, this.selection)).findFirst().orElse(null));
      this.layout.visitWidgets(x$0 -> this.addRenderableWidget(x$0));
      this.repositionElements();
   }

   @Override
   protected void repositionElements() {
      this.layout.arrangeElements();
      if (this.list != null) {
         this.list.updateSize(this.width, this.layout);
      }
   }

   private void onDone() {
      this.applySettings.accept(this.selection.preference(), this.selection.region());
      this.onClose();
   }

   private void updateButtonValidity() {
      if (this.doneButton != null && this.list != null) {
         this.doneButton.active = this.list.getSelected() != null;
      }
   }
}
