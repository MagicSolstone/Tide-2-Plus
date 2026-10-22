package com.mojang.realmsclient.gui.screens.configuration;

import com.mojang.realmsclient.dto.RealmsRegion;
import com.mojang.realmsclient.dto.RegionSelectionPreference;
import com.mojang.realmsclient.dto.ServiceQuality;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList$Entry;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class RealmsPreferredRegionSelectionScreen$RegionSelectionList$Entry
   extends ObjectSelectionList$Entry<RealmsPreferredRegionSelectionScreen$RegionSelectionList$Entry> {
   private final RealmsSettingsTab$RegionSelection regionSelection;
   private final Component name;

   public RealmsPreferredRegionSelectionScreen$RegionSelectionList$Entry(
      final RealmsPreferredRegionSelectionScreen$RegionSelectionList this$1, @Nullable final RegionSelectionPreference preference, final RealmsRegion region
   ) {
      this(this$1x, new RealmsSettingsTab$RegionSelection(preference, region));
   }

   public RealmsPreferredRegionSelectionScreen$RegionSelectionList$Entry(
      final RealmsPreferredRegionSelectionScreen$RegionSelectionList this$1, final RealmsSettingsTab$RegionSelection regionSelection
   ) {
      Objects.requireNonNull(this$1x);
      this.this$1 = this$1x;
      super();
      this.regionSelection = regionSelection;
      if (regionSelection.preference() == RegionSelectionPreference.MANUAL) {
         if (regionSelection.region() != null) {
            this.name = Component.translatable(regionSelection.region().translationKey);
         } else {
            this.name = Component.empty();
         }
      } else {
         this.name = Component.translatable(regionSelection.preference().translationKey);
      }
   }

   @Override
   public Component getNarration() {
      return Component.translatable("narrator.select", this.name);
   }

   @Override
   public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
      graphics.text(RealmsPreferredRegionSelectionScreen.access$100(this.this$1.this$0), this.name, this.getContentX() + 5, this.getContentY() + 2, -1);
      if (this.regionSelection.region() != null && this.this$1.this$0.regionServiceQuality.containsKey(this.regionSelection.region())) {
         ServiceQuality serviceQuality = this.this$1.this$0.regionServiceQuality.getOrDefault(this.regionSelection.region(), ServiceQuality.UNKNOWN);
         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, serviceQuality.getIcon(), this.getContentRight() - 18, this.getContentY() + 2, 10, 8);
      }
   }

   @Override
   public boolean mouseClicked(final MouseButtonEvent event, final boolean doubleClick) {
      this.this$1.setSelected(this);
      if (doubleClick) {
         this.this$1.playDownSound(RealmsPreferredRegionSelectionScreen$RegionSelectionList.access$200(this.this$1).getSoundManager());
         this.this$1.this$0.onDone();
         return true;
      } else {
         return super.mouseClicked(event, doubleClick);
      }
   }

   @Override
   public boolean keyPressed(final KeyEvent event) {
      if (event.isSelection()) {
         this.this$1.playDownSound(RealmsPreferredRegionSelectionScreen$RegionSelectionList.access$300(this.this$1).getSoundManager());
         this.this$1.this$0.onDone();
         return true;
      } else {
         return super.keyPressed(event);
      }
   }
}
