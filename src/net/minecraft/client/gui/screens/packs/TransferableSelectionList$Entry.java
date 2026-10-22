package net.minecraft.client.gui.screens.packs;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.ObjectSelectionList$Entry;

@Environment(EnvType.CLIENT)
public abstract class TransferableSelectionList$Entry extends ObjectSelectionList$Entry<TransferableSelectionList$Entry> {
   public TransferableSelectionList$Entry(final TransferableSelectionList this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public int getWidth() {
      return super.getWidth() - (TransferableSelectionList.access$000(this.this$0) ? this.this$0.scrollbarWidth() : 0);
   }

   public abstract String getPackId();
}
