package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.dto.WorldTemplate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.input.MouseButtonEvent;

@Environment(EnvType.CLIENT)
class RealmsSelectWorldTemplateScreen$WorldTemplateList extends ObjectSelectionList<RealmsSelectWorldTemplateScreen$Entry> {
   public RealmsSelectWorldTemplateScreen$WorldTemplateList(final RealmsSelectWorldTemplateScreen this$0) {
      this(this$0x, Collections.emptyList());
   }

   public RealmsSelectWorldTemplateScreen$WorldTemplateList(final Iterable<WorldTemplate> this$0, final Iterable templates) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(Minecraft.getInstance(), this$0x.width, this$0x.layout.getContentHeight(), this$0x.layout.getHeaderHeight(), 46);
      templates.forEach(this::addEntry);
   }

   public void addEntry(final WorldTemplate template) {
      this.addEntry(new RealmsSelectWorldTemplateScreen$Entry(this.this$0, template));
   }

   @Override
   public boolean mouseClicked(final MouseButtonEvent event, final boolean doubleClick) {
      if (this.this$0.currentLink != null) {
         ConfirmLinkScreen.confirmLinkNow(this.this$0, this.this$0.currentLink);
         return true;
      } else {
         return super.mouseClicked(event, doubleClick);
      }
   }

   public void setSelected(final RealmsSelectWorldTemplateScreen$Entry selected) {
      super.setSelected(selected);
      this.this$0.selectedTemplate = selected == null ? null : selected.template;
      this.this$0.updateButtonStates();
   }

   @Override
   public int getRowWidth() {
      return 300;
   }

   public boolean isEmpty() {
      return this.getItemCount() == 0;
   }

   public List<WorldTemplate> getTemplates() {
      return this.children().stream().map(c -> c.template).collect(Collectors.toList());
   }
}
