package net.minecraft.client.gui.screens;

import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class CreateFlatWorldScreen extends Screen {
   private static final Component TITLE = Component.translatable("createWorld.customize.flat.title");
   private static final Identifier SLOT_SPRITE = Identifier.withDefaultNamespace("container/slot");
   private static final int SLOT_BG_SIZE = 18;
   private static final int SLOT_STAT_HEIGHT = 20;
   private static final int SLOT_BG_X = 1;
   private static final int SLOT_BG_Y = 1;
   private static final int SLOT_FG_X = 2;
   private static final int SLOT_FG_Y = 2;
   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this, 33, 64);
   protected final CreateWorldScreen parent;
   private final Consumer<FlatLevelGeneratorSettings> applySettings;
   private FlatLevelGeneratorSettings generator;
   private CreateFlatWorldScreen$DetailsList list;
   @Nullable
   private Button deleteLayerButton;

   public CreateFlatWorldScreen(
      final CreateWorldScreen parent, final Consumer<FlatLevelGeneratorSettings> applySettings, final FlatLevelGeneratorSettings generator
   ) {
      super(TITLE);
      this.parent = parent;
      this.applySettings = applySettings;
      this.generator = generator;
   }

   public FlatLevelGeneratorSettings settings() {
      return this.generator;
   }

   public void setConfig(final FlatLevelGeneratorSettings generator) {
      this.generator = generator;
      if (this.list != null) {
         this.list.resetRows();
         this.updateButtonValidity();
      }
   }

   @Override
   protected void init() {
      this.layout.addTitleHeader(this.title, this.font);
      this.list = this.layout.addToContents(new CreateFlatWorldScreen$DetailsList(this));
      LinearLayout footer = this.layout.addToFooter(LinearLayout.vertical().spacing(4));
      footer.defaultCellSetting().alignVerticallyMiddle();
      LinearLayout topFooterButtons = footer.addChild(LinearLayout.horizontal().spacing(8));
      LinearLayout bottomFooterButtons = footer.addChild(LinearLayout.horizontal().spacing(8));
      this.deleteLayerButton = topFooterButtons.addChild(Button.builder(Component.translatable("createWorld.customize.flat.removeLayer"), button -> {
         if (this.list != null && this.list.getSelected() instanceof CreateFlatWorldScreen$DetailsList$LayerEntry selectedLayerEntry) {
            this.list.deleteLayer(selectedLayerEntry);
         }
      }).build());
      topFooterButtons.addChild(Button.builder(Component.translatable("createWorld.customize.presets"), button -> {
         this.minecraft.gui.setScreen(new PresetFlatWorldScreen(this));
         this.generator.updateLayers();
         this.updateButtonValidity();
      }).build());
      bottomFooterButtons.addChild(Button.builder(CommonComponents.GUI_DONE, button -> {
         this.applySettings.accept(this.generator);
         this.onClose();
         this.generator.updateLayers();
      }).build());
      bottomFooterButtons.addChild(Button.builder(CommonComponents.GUI_CANCEL, button -> {
         this.onClose();
         this.generator.updateLayers();
      }).build());
      this.generator.updateLayers();
      this.updateButtonValidity();
      this.layout.visitWidgets(this::addRenderableWidget);
      this.repositionElements();
   }

   @Override
   protected void repositionElements() {
      if (this.list != null) {
         this.list.updateSize(this.width, this.layout);
      }

      this.layout.arrangeElements();
   }

   private void updateButtonValidity() {
      if (this.deleteLayerButton != null) {
         this.deleteLayerButton.active = this.hasValidSelection();
      }
   }

   private boolean hasValidSelection() {
      return this.list != null && this.list.getSelected() instanceof CreateFlatWorldScreen$DetailsList$LayerEntry;
   }

   @Override
   public void onClose() {
      this.minecraft.gui.setScreen(this.parent);
   }
}
