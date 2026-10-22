package net.minecraft.client.gui.components;

import com.google.common.collect.Lists;
import java.util.AbstractList;
import java.util.List;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class AbstractSelectionList$TrackedList extends AbstractList<E> {
   private final List<E> delegate;

   private AbstractSelectionList$TrackedList(final AbstractSelectionList this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.delegate = Lists.newArrayList();
   }

   public E get(final int index) {
      return (E)((AbstractSelectionList$Entry)this.delegate.get(index));
   }

   @Override
   public int size() {
      return this.delegate.size();
   }

   public E set(final int index, final E element) {
      E entry = (E)((AbstractSelectionList$Entry)this.delegate.set(index, element));
      this.this$0.bindEntryToSelf(element);
      return entry;
   }

   public void add(final int index, final E element) {
      this.delegate.add(index, element);
      this.this$0.bindEntryToSelf(element);
   }

   public E remove(final int index) {
      return (E)((AbstractSelectionList$Entry)this.delegate.remove(index));
   }
}
