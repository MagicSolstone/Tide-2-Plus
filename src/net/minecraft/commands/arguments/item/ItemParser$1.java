package net.minecraft.commands.arguments.item;

import java.util.Objects;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch$Builder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.Item;
import org.apache.commons.lang3.mutable.MutableObject;

class ItemParser$1 implements ItemParser$Visitor {
   ItemParser$1(final ItemParser this$0, final MutableObject val$itemResult, final DataComponentPatch$Builder val$componentsBuilder) {
      this.val$itemResult = val$itemResultx;
      this.val$componentsBuilder = val$componentsBuilderx;
      Objects.requireNonNull(this$0);
      super();
   }

   @Override
   public void visitItem(final Holder<Item> item) {
      this.val$itemResult.setValue(item);
   }

   @Override
   public <T> void visitComponent(final DataComponentType<T> type, final T value) {
      this.val$componentsBuilder.set(type, value);
   }

   @Override
   public <T> void visitRemovedComponent(final DataComponentType<T> type) {
      this.val$componentsBuilder.remove(type);
   }
}
