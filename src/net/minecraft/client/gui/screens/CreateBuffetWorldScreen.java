package net.minecraft.client.gui.screens;

import java.util.Objects;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.worldselection.WorldCreationContext;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

@Environment(EnvType.CLIENT)
public class CreateBuffetWorldScreen extends Screen {
   private static final Component SEARCH_HINT = Component.translatable("createWorld.customize.buffet.search").withStyle(EditBox.SEARCH_HINT_STYLE);
   private static final int SPACING = 3;
   private static final int SEARCH_BOX_HEIGHT = 15;
   private final HeaderAndFooterLayout layout;
   private final Screen parent;
   private final Consumer<Holder<Biome>> applySettings;
   private final Registry<Biome> biomes;
   private CreateBuffetWorldScreen$BiomeList list;
   private Holder<Biome> biome;
   private Button doneButton;

   public CreateBuffetWorldScreen(final Screen parent, final WorldCreationContext settings, final Consumer<Holder<Biome>> applySettings) {
      super(Component.translatable("createWorld.customize.buffet.title"));
      this.parent = parent;
      this.applySettings = applySettings;
      this.layout = new HeaderAndFooterLayout(this, 13 + 9 + 3 + 15, 33);
      this.biomes = settings.worldgenLoadContext().lookupOrThrow(Registries.BIOME);
      Holder<Biome> defaultBiome = this.biomes.get(Biomes.PLAINS).or(() -> this.biomes.listElements().findAny()).orElseThrow();
      this.biome = settings.selectedDimensions().overworld().getBiomeSource().possibleBiomes().stream().findFirst().orElse(defaultBiome);
   }

   @Override
   public void onClose() {
      this.minecraft.gui.setScreen(this.parent);
   }

   @Override
   protected void init() {
      LinearLayout header = this.layout.addToHeader(LinearLayout.vertical().spacing(3));
      header.defaultCellSetting().alignHorizontallyCenter();
      header.addChild(new StringWidget(this.getTitle(), this.font));
      EditBox search = header.addChild(new EditBox(this.font, 200, 15, Component.empty()));
      CreateBuffetWorldScreen$BiomeList biomeList = new CreateBuffetWorldScreen$BiomeList(this);
      search.setHint(SEARCH_HINT);
      search.setResponder(biomeList::filterEntries);
      this.list = this.layout.addToContents(biomeList);
      LinearLayout footer = this.layout.addToFooter(LinearLayout.horizontal().spacing(8));
      this.doneButton = footer.addChild(Button.builder(CommonComponents.GUI_DONE, button -> {
         this.applySettings.accept(this.biome);
         this.onClose();
      }).build());
      footer.addChild(Button.builder(CommonComponents.GUI_CANCEL, button -> this.onClose()).build());
      this.list.setSelected(this.list.children().stream().filter(e -> Objects.equals(e.biome, this.biome)).findFirst().orElse(null));
      this.layout.visitWidgets(this::addRenderableWidget);
      this.repositionElements();
   }

   @Override
   protected void repositionElements() {
      this.layout.arrangeElements();
      this.list.updateSize(this.width, this.layout);
   }

   private void updateButtonValidity() {
      this.doneButton.active = this.list.getSelected() != null;
   }
}
