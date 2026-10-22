package net.minecraft.client.gui.screens.packs;

import java.util.List;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.packs.repository.Pack;

@Environment(EnvType.CLIENT)
class PackSelectionModel$UnselectedPackEntry extends PackSelectionModel$EntryBase {
   public PackSelectionModel$UnselectedPackEntry(final PackSelectionModel this$0, final Pack pack) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(this$0x, pack);
   }

   @Override
   protected List<Pack> getSelfList() {
      return this.this$0.unselected;
   }

   @Override
   protected List<Pack> getOtherList() {
      return this.this$0.selected;
   }

   @Override
   public boolean isSelected() {
      return false;
   }

   @Override
   public void select() {
      this.toggleSelection();
   }

   @Override
   public void unselect() {
   }
}
