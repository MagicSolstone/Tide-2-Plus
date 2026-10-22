package com.mojang.realmsclient.gui.screens.configuration;

import com.mojang.realmsclient.dto.RealmTierConfigurationDto;
import com.mojang.realmsclient.dto.RealmTierConfigurationDto$RealmTierRangeDto;
import com.mojang.realmsclient.dto.RealmsRegion;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.dto.RealmsServer$State;
import com.mojang.realmsclient.dto.RegionSelectionPreference;
import com.mojang.realmsclient.dto.RegionSelectionPreferenceDto;
import com.mojang.realmsclient.dto.ServiceQuality;
import com.mojang.realmsclient.gui.screens.RealmsPopups;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.OptionInstance$IntRange;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ImageWidget;
import net.minecraft.client.gui.components.ScrollableLayout;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.tabs.GridLayoutTab;
import net.minecraft.client.gui.layouts.CommonLayouts;
import net.minecraft.client.gui.layouts.EqualSpacingLayout;
import net.minecraft.client.gui.layouts.EqualSpacingLayout$Orientation;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class RealmsSettingsTab extends GridLayoutTab implements RealmsConfigurationTab {
   private static final int COMPONENT_WIDTH = 212;
   public static final Component TITLE = Component.translatable("mco.configure.world.settings.title");
   private static final Component NAME_LABEL = Component.translatable("mco.configure.world.name");
   private static final Component DESCRIPTION_LABEL = Component.translatable("mco.configure.world.description");
   private static final Component RENDER_DISTANCE_LABEL = Component.translatable("mco.configure.world.renderDistance");
   private static final Component RENDER_DISTANCE_TOOLTIP = Component.translatable("mco.configure.world.renderDistance.tooltip");
   private static final Component SIMULATION_DISTANCE_LABEL = Component.translatable("mco.configure.world.simulationDistance");
   private static final Component SIMULATION_DISTANCE_TOOLTIP = Component.translatable("mco.configure.world.simulationDistance.tooltip");
   private static final Component REGION_PREFERENCE_LABEL = Component.translatable("mco.configure.world.region_preference");
   private static final Tooltip REALM_NAME_VALIDATION_ERROR_TOOLTIP = Tooltip.create(Component.translatable("mco.configure.world.name.validation.whitespace"));
   private final RealmsConfigureWorldScreen configurationScreen;
   private final Minecraft minecraft;
   private RealmsServer serverData;
   private final Map<RealmsRegion, ServiceQuality> regionServiceQuality;
   private final ScrollableLayout scrollableLayout;
   private final Button closeOpenButton;
   private final EditBox descEdit;
   private final EditBox nameEdit;
   @Nullable
   private final OptionInstance<Integer> renderDistanceOption;
   @Nullable
   private final OptionInstance<Integer> simulationDistanceOption;
   private int initialRenderDistance;
   private int initialSimulationDistance;
   private final StringWidget selectedRegionStringWidget;
   private final ImageWidget selectedRegionImageWidget;
   private RealmsSettingsTab$RegionSelection preferredRegionSelection;

   public RealmsSettingsTab(
      final RealmsConfigureWorldScreen configurationScreen,
      final Minecraft minecraft,
      final RealmsServer serverData,
      @Nullable final RealmTierConfigurationDto tierConfiguration,
      final Map<RealmsRegion, ServiceQuality> regionServiceQuality
   ) {
      super(TITLE);
      this.configurationScreen = configurationScreen;
      this.minecraft = minecraft;
      this.serverData = serverData;
      this.regionServiceQuality = regionServiceQuality;
      if (tierConfiguration != null) {
         RealmTierConfigurationDto$RealmTierRangeDto renderDistanceRange = tierConfiguration.renderDistance();
         this.initialRenderDistance = currentOrDefault(renderDistanceRange);
         this.renderDistanceOption = createDistanceOption(
            "mco.configure.world.renderDistance", RENDER_DISTANCE_TOOLTIP, renderDistanceRange, this.initialRenderDistance
         );
         RealmTierConfigurationDto$RealmTierRangeDto simulationDistanceRange = tierConfiguration.simDistance();
         this.initialSimulationDistance = currentOrDefault(simulationDistanceRange);
         this.simulationDistanceOption = createDistanceOption(
            "mco.configure.world.simulationDistance", SIMULATION_DISTANCE_TOOLTIP, simulationDistanceRange, this.initialSimulationDistance
         );
      } else {
         this.renderDistanceOption = null;
         this.simulationDistanceOption = null;
      }

      LinearLayout content = LinearLayout.vertical();
      content.defaultCellSetting().padding(8);
      this.scrollableLayout = this.layout.addChild(new ScrollableLayout(minecraft, content, configurationScreen.getContentHeight()), 0, 0);
      Font font = configurationScreen.getFont();
      this.nameEdit = new EditBox(minecraft.font, 0, 0, 212, 20, Component.translatable("mco.configure.world.name"));
      this.nameEdit.setMaxLength(32);
      this.nameEdit.setResponder(value -> {
         if (!this.isRealmNameValid()) {
            this.nameEdit.setTextColor(-2142128);
            this.nameEdit.setTooltip(REALM_NAME_VALIDATION_ERROR_TOOLTIP);
         } else {
            this.nameEdit.setTooltip(null);
            this.nameEdit.setTextColor(-2039584);
         }
      });
      content.addChild(CommonLayouts.labeledElement(font, this.nameEdit, NAME_LABEL));
      this.descEdit = new EditBox(minecraft.font, 0, 0, 212, 20, Component.translatable("mco.configure.world.description"));
      this.descEdit.setMaxLength(32);
      content.addChild(CommonLayouts.labeledElement(font, this.descEdit, DESCRIPTION_LABEL));
      if (this.renderDistanceOption != null && this.simulationDistanceOption != null) {
         content.addChild(CommonLayouts.labeledElement(font, this.renderDistanceOption.createButton(minecraft.options, 0, 0, 212), RENDER_DISTANCE_LABEL));
         content.addChild(
            CommonLayouts.labeledElement(font, this.simulationDistanceOption.createButton(minecraft.options, 0, 0, 212), SIMULATION_DISTANCE_LABEL)
         );
      }

      LinearLayout selectedRegionAndButton = LinearLayout.vertical();
      EqualSpacingLayout selectedRegion = selectedRegionAndButton.addChild(new EqualSpacingLayout(0, 0, 212, 9, EqualSpacingLayout$Orientation.HORIZONTAL));
      this.selectedRegionStringWidget = selectedRegion.addChild(new StringWidget(192, 9, Component.empty(), font));
      this.selectedRegionImageWidget = selectedRegion.addChild(ImageWidget.sprite(10, 8, ServiceQuality.UNKNOWN.getIcon()));
      selectedRegionAndButton.addChild(
         Button.builder(Component.translatable("mco.configure.world.buttons.region_preference"), button -> this.openPreferenceSelector())
            .bounds(0, 0, 212, 20)
            .build()
      );
      content.addChild(CommonLayouts.labeledElement(font, selectedRegionAndButton, REGION_PREFERENCE_LABEL));
      this.closeOpenButton = content.addChild(
         Button.builder(
               Component.empty(),
               button -> {
                  if (serverData.state == RealmsServer$State.OPEN) {
                     minecraft.gui
                        .setScreen(
                           RealmsPopups.customPopupScreen(
                              configurationScreen,
                              Component.translatable("mco.configure.world.close.question.title"),
                              Component.translatable("mco.configure.world.close.question.line1"),
                              popup -> {
                                 if (this.save()) {
                                    configurationScreen.closeTheWorld();
                                 }
                              }
                           )
                        );
                  } else if (this.save()) {
                     configurationScreen.openTheWorld(false);
                  }
               }
            )
            .bounds(0, 0, 212, 20)
            .build(),
         content.newCellSettings().paddingBottom(8)
      );
      this.closeOpenButton.active = false;
      this.updateData(serverData);
   }

   private static int currentOrDefault(final RealmTierConfigurationDto$RealmTierRangeDto range) {
      return Objects.requireNonNullElse(range.current(), range.defaultValue());
   }

   private static OptionInstance<Integer> createDistanceOption(
      final String captionId, final Component tooltip, final RealmTierConfigurationDto$RealmTierRangeDto range, final int initialValue
   ) {
      return new OptionInstance<>(
         captionId,
         OptionInstance.cachedConstantTooltip(tooltip),
         (var0, value) -> Component.translatable("options.chunks", value),
         new OptionInstance$IntRange(range.min(), range.max()),
         initialValue,
         var0 -> {}
      );
   }

   private static MutableComponent getTranslatableFromPreference(final RealmsSettingsTab$RegionSelection regionSelection) {
      return (regionSelection.preference().equals(RegionSelectionPreference.MANUAL) && regionSelection.region() != null
            ? Component.translatable(regionSelection.region().translationKey)
            : Component.translatable(regionSelection.preference().translationKey))
         .withStyle(ChatFormatting.GRAY);
   }

   private static Identifier getServiceQualityIcon(
      final RealmsSettingsTab$RegionSelection regionSelection, final Map<RealmsRegion, ServiceQuality> regionServiceQuality
   ) {
      if (regionSelection.region() != null && regionServiceQuality.containsKey(regionSelection.region())) {
         ServiceQuality serviceQuality = regionServiceQuality.getOrDefault(regionSelection.region(), ServiceQuality.UNKNOWN);
         return serviceQuality.getIcon();
      } else {
         return ServiceQuality.UNKNOWN.getIcon();
      }
   }

   private boolean isRealmNameValid() {
      String name = this.nameEdit.getValue();
      String trimmedName = name.trim();
      return !trimmedName.isEmpty() && name.length() == trimmedName.length();
   }

   private void openPreferenceSelector() {
      this.minecraft
         .gui
         .setScreen(
            new RealmsPreferredRegionSelectionScreen(
               this.configurationScreen, this::applyRegionPreferenceSelection, this.regionServiceQuality, this.preferredRegionSelection
            )
         );
   }

   private void applyRegionPreferenceSelection(final RegionSelectionPreference preference, @Nullable final RealmsRegion region) {
      this.preferredRegionSelection = new RealmsSettingsTab$RegionSelection(preference, region);
      this.updateRegionPreferenceValues();
   }

   private void updateRegionPreferenceValues() {
      this.selectedRegionStringWidget.setMessage(getTranslatableFromPreference(this.preferredRegionSelection));
      this.selectedRegionImageWidget.updateResource(getServiceQualityIcon(this.preferredRegionSelection, this.regionServiceQuality));
      this.selectedRegionImageWidget.visible = this.preferredRegionSelection.preference == RegionSelectionPreference.MANUAL;
   }

   @Override
   public void doLayout(final ScreenRectangle screenRectangle) {
      this.scrollableLayout.arrangeElements();
      this.scrollableLayout.setMaxHeight(screenRectangle.height() - 2);
      super.doLayout(screenRectangle);
   }

   @Override
   public void onSelected(final RealmsServer serverData) {
      this.updateData(serverData);
   }

   @Override
   public void updateData(final RealmsServer serverData) {
      this.serverData = serverData;
      if (serverData.regionSelectionPreference == null) {
         serverData.regionSelectionPreference = RegionSelectionPreferenceDto.DEFAULT;
      }

      if (serverData.regionSelectionPreference.regionSelectionPreference == RegionSelectionPreference.MANUAL
         && serverData.regionSelectionPreference.preferredRegion == null) {
         Optional<RealmsRegion> first = this.regionServiceQuality.keySet().stream().findFirst();
         first.ifPresent(region -> serverData.regionSelectionPreference.preferredRegion = region);
      }

      String key = serverData.state == RealmsServer$State.OPEN ? "mco.configure.world.buttons.close" : "mco.configure.world.buttons.open";
      this.closeOpenButton.setMessage(Component.translatable(key));
      this.closeOpenButton.active = true;
      this.preferredRegionSelection = new RealmsSettingsTab$RegionSelection(
         serverData.regionSelectionPreference.regionSelectionPreference, serverData.regionSelectionPreference.preferredRegion
      );
      this.nameEdit.setValue(Objects.requireNonNullElse(serverData.getName(), ""));
      this.descEdit.setValue(serverData.getDescription());
      this.updateRegionPreferenceValues();
   }

   @Override
   public void onDeselected(final RealmsServer serverData) {
      this.save();
   }

   public boolean save() {
      String realmName = this.nameEdit.getValue().trim();
      int renderDistance = this.renderDistanceOption == null ? this.initialRenderDistance : this.renderDistanceOption.get();
      int simulationDistance = this.simulationDistanceOption == null ? this.initialSimulationDistance : this.simulationDistanceOption.get();
      int previousRenderDistance = this.initialRenderDistance;
      int previousSimulationDistance = this.initialSimulationDistance;
      Integer updatedRenderDistance = renderDistance == this.initialRenderDistance ? null : renderDistance;
      Integer updatedSimulationDistance = simulationDistance == this.initialSimulationDistance ? null : simulationDistance;
      if (this.serverData.regionSelectionPreference != null
         && Objects.equals(realmName, this.serverData.name)
         && Objects.equals(this.descEdit.getValue(), this.serverData.motd)
         && this.preferredRegionSelection.preference() == this.serverData.regionSelectionPreference.regionSelectionPreference
         && this.preferredRegionSelection.region() == this.serverData.regionSelectionPreference.preferredRegion
         && updatedRenderDistance == null
         && updatedSimulationDistance == null) {
         return true;
      } else {
         this.initialRenderDistance = renderDistance;
         this.initialSimulationDistance = simulationDistance;
         boolean saved = this.configurationScreen
            .saveSettings(
               realmName,
               this.descEdit.getValue(),
               this.preferredRegionSelection.preference(),
               this.preferredRegionSelection.region(),
               updatedRenderDistance,
               updatedSimulationDistance
            );
         if (!saved) {
            this.initialRenderDistance = previousRenderDistance;
            this.initialSimulationDistance = previousSimulationDistance;
         }

         return saved;
      }
   }
}
