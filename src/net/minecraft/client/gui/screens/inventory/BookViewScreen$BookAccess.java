package net.minecraft.client.gui.screens.inventory;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.WritableBookContent;
import net.minecraft.world.item.component.WrittenBookContent;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record BookViewScreen$BookAccess(List<Component> pages) {
   public int getPageCount() {
      return this.pages.size();
   }

   public Component getPage(final int page) {
      return page >= 0 && page < this.getPageCount() ? this.pages.get(page) : CommonComponents.EMPTY;
   }

   @Nullable
   public static BookViewScreen$BookAccess fromItem(final ItemStack itemStack) {
      boolean filterEnabled = Minecraft.getInstance().isTextFilteringEnabled();
      WrittenBookContent writtenContent = itemStack.get(DataComponents.WRITTEN_BOOK_CONTENT);
      if (writtenContent != null) {
         return new BookViewScreen$BookAccess(writtenContent.getPages(filterEnabled));
      } else {
         WritableBookContent writableContent = itemStack.get(DataComponents.WRITABLE_BOOK_CONTENT);
         return writableContent != null ? new BookViewScreen$BookAccess(writableContent.getPages(filterEnabled).map(Component::literal).toList()) : null;
      }
   }
}
