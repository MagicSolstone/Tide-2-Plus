package net.minecraft.client.gui.screens;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList$Entry;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorPreset;

@Environment(EnvType.CLIENT)
public class PresetFlatWorldScreen$PresetsList$Entry extends ObjectSelectionList$Entry<PresetFlatWorldScreen$PresetsList$Entry> {
   private final FlatLevelGeneratorPreset preset;
   private final Component name;

   public PresetFlatWorldScreen$PresetsList$Entry(final Holder<FlatLevelGeneratorPreset> this$1, final Holder preset) {
      Objects.requireNonNull(this$1);
      this.this$1 = this$1;
      super();
      this.preset = preset.value();
      this.name = preset.unwrapKey()
         .map(key -> Component.translatable(key.identifier().toLanguageKey("flat_world_preset")))
         .orElse(PresetFlatWorldScreen.UNKNOWN_PRESET);
   }

   @Override
   public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
      this.blitSlot(graphics, this.getContentX(), this.getContentY(), this.preset.displayItem().value());
      graphics.text(this.this$1.this$0.font, this.name, this.getContentX() + 18 + 5, this.getContentY() + 6, -1);
   }

   @Override
   public boolean mouseClicked(final MouseButtonEvent event, final boolean doubleClick) {
      this.select();
      return super.mouseClicked(event, doubleClick);
   }

   private void select() {
      this.this$1.setSelected(this);
      this.this$1.this$0.settings = this.preset.settings();
      this.this$1.this$0.export.setValue(PresetFlatWorldScreen.save(this.this$1.this$0.settings));
      this.this$1.this$0.export.moveCursorToStart(false);
   }

   private void blitSlot(final GuiGraphicsExtractor graphics, final int x, final int y, final Item item) {
      this.blitSlotBg(graphics, x + 1, y + 1);
      graphics.fakeItem(new ItemStack(item), x + 2, y + 2);
   }

   private void blitSlotBg(final GuiGraphicsExtractor graphics, final int x, final int y) {
      graphics.blitSprite(RenderPipelines.GUI_TEXTURED, PresetFlatWorldScreen.SLOT_SPRITE, x, y, 18, 18);
   }

   @Override
   public Component getNarration() {
      return Component.translatable("narrator.select", this.name);
   }
}
