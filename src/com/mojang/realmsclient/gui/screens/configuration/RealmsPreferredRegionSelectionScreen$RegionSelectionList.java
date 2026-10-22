package com.mojang.realmsclient.gui.screens.configuration;

import com.mojang.realmsclient.dto.RegionSelectionPreference;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.ObjectSelectionList;

@Environment(EnvType.CLIENT)
class RealmsPreferredRegionSelectionScreen$RegionSelectionList extends ObjectSelectionList<RealmsPreferredRegionSelectionScreen$RegionSelectionList$Entry> {
   private RealmsPreferredRegionSelectionScreen$RegionSelectionList(final RealmsPreferredRegionSelectionScreen this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(RealmsPreferredRegionSelectionScreen.access$000(this$0x), this$0x.width, this$0x.height - 77, 40, 16);
      this.addEntry(new RealmsPreferredRegionSelectionScreen$RegionSelectionList$Entry(this, RegionSelectionPreference.AUTOMATIC_PLAYER, null));
      this.addEntry(new RealmsPreferredRegionSelectionScreen$RegionSelectionList$Entry(this, RegionSelectionPreference.AUTOMATIC_OWNER, null));
      this$0x.regionServiceQuality
         .keySet()
         .stream()
         .map(region -> new RealmsPreferredRegionSelectionScreen$RegionSelectionList$Entry(this, RegionSelectionPreference.MANUAL, region))
         .forEach(x$0 -> this.addEntry(x$0));
   }

   public void setSelected(final RealmsPreferredRegionSelectionScreen$RegionSelectionList$Entry selected) {
      super.setSelected(selected);
      if (selected != null) {
         this.this$0.selection = selected.regionSelection;
      }

      this.this$0.updateButtonValidity();
   }
}
