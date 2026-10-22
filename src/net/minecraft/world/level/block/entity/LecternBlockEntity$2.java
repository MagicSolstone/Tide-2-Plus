package net.minecraft.world.level.block.entity;

import java.util.Objects;
import net.minecraft.world.inventory.ContainerData;

class LecternBlockEntity$2 implements ContainerData {
   LecternBlockEntity$2(final LecternBlockEntity this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public int get(final int dataId) {
      return dataId == 0 ? this.this$0.page : 0;
   }

   @Override
   public void set(final int dataId, final int value) {
      if (dataId == 0) {
         this.this$0.setPage(value);
      }
   }

   @Override
   public int getCount() {
      return 1;
   }
}
