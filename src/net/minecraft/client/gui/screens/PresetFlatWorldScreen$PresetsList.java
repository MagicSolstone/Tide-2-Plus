package net.minecraft.client.gui.screens;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.FlatLevelGeneratorPresetTags;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorPreset;

@Environment(EnvType.CLIENT)
class PresetFlatWorldScreen$PresetsList extends ObjectSelectionList<PresetFlatWorldScreen$PresetsList$Entry> {
   public PresetFlatWorldScreen$PresetsList(final PresetFlatWorldScreen this$0, final RegistryAccess access, final FeatureFlagSet enabledFeatures) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(this$0x.minecraft, this$0x.width, this$0x.height - 117, 80, 24);

      for (Holder<FlatLevelGeneratorPreset> preset : access.lookupOrThrow(Registries.FLAT_LEVEL_GENERATOR_PRESET)
         .getTagOrEmpty(FlatLevelGeneratorPresetTags.VISIBLE)) {
         Set<Block> disabledBlocks = preset.value()
            .settings()
            .getLayersInfo()
            .stream()
            .map(p -> p.getBlockState().getBlock())
            .filter(b -> !b.isEnabled(enabledFeatures))
            .collect(Collectors.toSet());
         if (!disabledBlocks.isEmpty()) {
            PresetFlatWorldScreen.LOGGER
               .info(
                  "Discarding flat world preset {} since it contains experimental blocks {}",
                  preset.unwrapKey().map(e -> e.identifier().toString()).orElse("<unknown>"),
                  disabledBlocks
               );
         } else {
            this.addEntry(new PresetFlatWorldScreen$PresetsList$Entry(this, preset));
         }
      }
   }

   public void setSelected(final PresetFlatWorldScreen$PresetsList$Entry selected) {
      super.setSelected(selected);
      this.this$0.updateButtonValidity(selected != null);
   }

   @Override
   public boolean keyPressed(final KeyEvent event) {
      if (super.keyPressed(event)) {
         return true;
      } else {
         if (event.isSelection() && this.getSelected() != null) {
            this.getSelected().select();
         }

         return false;
      }
   }
}
