package net.minecraft.world.item;

import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class CreativeModeTab$Builder {
   private static final CreativeModeTab$DisplayItemsGenerator EMPTY_GENERATOR = (parameters, output) -> {};
   private final CreativeModeTab$Row row;
   private final int column;
   private Component displayName = Component.empty();
   private Supplier<ItemStack> iconGenerator = () -> ItemStack.EMPTY;
   private CreativeModeTab$DisplayItemsGenerator displayItemsGenerator = EMPTY_GENERATOR;
   private boolean canScroll = true;
   private boolean showTitle = true;
   private boolean alignedRight = false;
   private CreativeModeTab$Type type = CreativeModeTab$Type.CATEGORY;
   private Identifier backgroundTexture = CreativeModeTab.DEFAULT_BACKGROUND;

   public CreativeModeTab$Builder(final CreativeModeTab$Row row, final int column) {
      this.row = row;
      this.column = column;
   }

   public CreativeModeTab$Builder title(final Component displayName) {
      this.displayName = displayName;
      return this;
   }

   public CreativeModeTab$Builder icon(final Supplier<ItemStack> iconGenerator) {
      this.iconGenerator = iconGenerator;
      return this;
   }

   public CreativeModeTab$Builder displayItems(final CreativeModeTab$DisplayItemsGenerator displayItemsGenerator) {
      this.displayItemsGenerator = displayItemsGenerator;
      return this;
   }

   public CreativeModeTab$Builder alignedRight() {
      this.alignedRight = true;
      return this;
   }

   public CreativeModeTab$Builder hideTitle() {
      this.showTitle = false;
      return this;
   }

   public CreativeModeTab$Builder noScrollBar() {
      this.canScroll = false;
      return this;
   }

   protected CreativeModeTab$Builder type(final CreativeModeTab$Type type) {
      this.type = type;
      return this;
   }

   public CreativeModeTab$Builder backgroundTexture(final Identifier backgroundTexture) {
      this.backgroundTexture = backgroundTexture;
      return this;
   }

   public CreativeModeTab build() {
      if ((this.type == CreativeModeTab$Type.HOTBAR || this.type == CreativeModeTab$Type.INVENTORY) && this.displayItemsGenerator != EMPTY_GENERATOR) {
         throw new IllegalStateException("Special tabs can't have display items");
      } else {
         CreativeModeTab tab = new CreativeModeTab(this.row, this.column, this.type, this.displayName, this.iconGenerator, this.displayItemsGenerator);
         tab.alignedRight = this.alignedRight;
         tab.showTitle = this.showTitle;
         tab.canScroll = this.canScroll;
         tab.backgroundTexture = this.backgroundTexture;
         return tab;
      }
   }
}
