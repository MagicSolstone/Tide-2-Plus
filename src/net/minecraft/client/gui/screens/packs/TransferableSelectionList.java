package net.minecraft.client.gui.screens.packs;

import java.util.stream.Stream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public class TransferableSelectionList extends ObjectSelectionList<TransferableSelectionList$Entry> {
   private static final Identifier SELECT_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("transferable_list/select_highlighted");
   private static final Identifier SELECT_SPRITE = Identifier.withDefaultNamespace("transferable_list/select");
   private static final Identifier UNSELECT_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("transferable_list/unselect_highlighted");
   private static final Identifier UNSELECT_SPRITE = Identifier.withDefaultNamespace("transferable_list/unselect");
   private static final Identifier MOVE_UP_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("transferable_list/move_up_highlighted");
   private static final Identifier MOVE_UP_SPRITE = Identifier.withDefaultNamespace("transferable_list/move_up");
   private static final Identifier MOVE_DOWN_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("transferable_list/move_down_highlighted");
   private static final Identifier MOVE_DOWN_SPRITE = Identifier.withDefaultNamespace("transferable_list/move_down");
   private static final Component INCOMPATIBLE_TITLE = Component.translatable("pack.incompatible");
   private static final Component INCOMPATIBLE_CONFIRM_TITLE = Component.translatable("pack.incompatible.confirm.title");
   private static final int ENTRY_PADDING = 2;
   private final Component title;
   private final PackSelectionScreen screen;

   public TransferableSelectionList(final Minecraft minecraft, final PackSelectionScreen screen, final int width, final int height, final Component title) {
      super(minecraft, width, height, 33, 36);
      this.screen = screen;
      this.title = title;
      this.centerListVertically = false;
   }

   @Override
   public int getRowWidth() {
      return this.width - 4;
   }

   @Override
   protected int scrollBarX() {
      return this.getRight() - this.scrollbarWidth();
   }

   @Override
   public boolean keyPressed(final KeyEvent event) {
      return this.getSelected() != null ? this.getSelected().keyPressed(event) : super.keyPressed(event);
   }

   public void updateList(final Stream<PackSelectionModel$Entry> entries, final PackSelectionModel$EntryBase transferredEntry) {
      this.clearEntries();
      Component header = Component.empty().append(this.title).withStyle(ChatFormatting.UNDERLINE, ChatFormatting.BOLD);
      this.addEntry(new TransferableSelectionList$HeaderEntry(this, this.minecraft.font, header), (int)(9.0F * 1.5F));
      this.setSelected(null);
      entries.forEach(e -> {
         TransferableSelectionList$PackEntry entry = new TransferableSelectionList$PackEntry(this, this.minecraft, this, e);
         this.addEntry(entry);
         if (transferredEntry != null && transferredEntry.getId().equals(e.getId())) {
            this.screen.setFocused(this);
            this.setFocused(entry);
         }
      });
      this.refreshScrollAmount();
   }
}
