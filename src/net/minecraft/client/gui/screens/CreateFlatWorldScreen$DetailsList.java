package net.minecraft.client.gui.screens;

import java.util.List;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.levelgen.flat.FlatLayerInfo;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class CreateFlatWorldScreen$DetailsList extends ObjectSelectionList<CreateFlatWorldScreen$DetailsList$Entry> {
   private static final Component LAYER_MATERIAL_TITLE = Component.translatable("createWorld.customize.flat.tile").withStyle(ChatFormatting.UNDERLINE);
   private static final Component HEIGHT_TITLE = Component.translatable("createWorld.customize.flat.height").withStyle(ChatFormatting.UNDERLINE);

   public CreateFlatWorldScreen$DetailsList(final CreateFlatWorldScreen this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(this$0x.minecraft, this$0x.width, this$0x.height - 103, 43, 24);
      this.populateList();
   }

   private void populateList() {
      this.addEntry(new CreateFlatWorldScreen$DetailsList$HeaderEntry(this.this$0.font), (int)(9.0 * 1.5));
      List<FlatLayerInfo> layersInfo = this.this$0.generator.getLayersInfo().reversed();

      for (int i = 0; i < layersInfo.size(); i++) {
         this.addEntry(new CreateFlatWorldScreen$DetailsList$LayerEntry(this, layersInfo.get(i), i));
      }
   }

   public void setSelected(@Nullable final CreateFlatWorldScreen$DetailsList$Entry selected) {
      super.setSelected(selected);
      this.this$0.updateButtonValidity();
   }

   public void resetRows() {
      int index = this.children().indexOf(this.getSelected());
      this.clearEntries();
      this.populateList();
      List<CreateFlatWorldScreen$DetailsList$Entry> children = this.children();
      if (index >= 0 && index < children.size()) {
         this.setSelected(children.get(index));
      }
   }

   private void deleteLayer(final CreateFlatWorldScreen$DetailsList$LayerEntry selectedLayerEntry) {
      List<FlatLayerInfo> layersInfo = this.this$0.generator.getLayersInfo();
      int deletedLayerIndex = this.children().indexOf(selectedLayerEntry);
      this.removeEntry(selectedLayerEntry);
      layersInfo.remove(selectedLayerEntry.layerInfo);
      this.setSelected(layersInfo.isEmpty() ? null : this.children().get(Math.min(deletedLayerIndex, layersInfo.size())));
      this.this$0.generator.updateLayers();
      this.resetRows();
      this.this$0.updateButtonValidity();
   }
}
