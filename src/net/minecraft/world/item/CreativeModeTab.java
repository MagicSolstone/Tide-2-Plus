package net.minecraft.world.item;

import java.util.Collection;
import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public class CreativeModeTab {
   private static final Identifier DEFAULT_BACKGROUND = createTextureLocation("items");
   private final Component displayName;
   private Identifier backgroundTexture = DEFAULT_BACKGROUND;
   private boolean canScroll = true;
   private boolean showTitle = true;
   private boolean alignedRight = false;
   private final CreativeModeTab$Row row;
   private final int column;
   private final CreativeModeTab$Type type;
   @Nullable
   private ItemStack iconItemStack;
   private Collection<ItemStack> displayItems = ItemStackLinkedSet.createTypeAndComponentsSet();
   private Set<ItemStack> displayItemsSearchTab = ItemStackLinkedSet.createTypeAndComponentsSet();
   private final Supplier<ItemStack> iconGenerator;
   private final CreativeModeTab$DisplayItemsGenerator displayItemsGenerator;

   private CreativeModeTab(
      final CreativeModeTab$Row row,
      final int column,
      final CreativeModeTab$Type type,
      final Component displayName,
      final Supplier<ItemStack> iconGenerator,
      final CreativeModeTab$DisplayItemsGenerator displayItemsGenerator
   ) {
      this.row = row;
      this.column = column;
      this.displayName = displayName;
      this.iconGenerator = iconGenerator;
      this.displayItemsGenerator = displayItemsGenerator;
      this.type = type;
   }

   public static Identifier createTextureLocation(final String name) {
      return Identifier.withDefaultNamespace("textures/gui/container/creative_inventory/tab_" + name + ".png");
   }

   public static CreativeModeTab$Builder builder(final CreativeModeTab$Row row, final int column) {
      return new CreativeModeTab$Builder(row, column);
   }

   public Component getDisplayName() {
      return this.displayName;
   }

   public ItemStack getIconItem() {
      if (this.iconItemStack == null) {
         this.iconItemStack = this.iconGenerator.get();
      }

      return this.iconItemStack;
   }

   public Identifier getBackgroundTexture() {
      return this.backgroundTexture;
   }

   public boolean showTitle() {
      return this.showTitle;
   }

   public boolean canScroll() {
      return this.canScroll;
   }

   public int column() {
      return this.column;
   }

   public CreativeModeTab$Row row() {
      return this.row;
   }

   public boolean hasAnyItems() {
      return !this.displayItems.isEmpty();
   }

   public boolean shouldDisplay() {
      return this.type != CreativeModeTab$Type.CATEGORY || this.hasAnyItems();
   }

   public boolean isAlignedRight() {
      return this.alignedRight;
   }

   public CreativeModeTab$Type getType() {
      return this.type;
   }

   public void buildContents(final CreativeModeTab$ItemDisplayParameters parameters) {
      CreativeModeTab$ItemDisplayBuilder displayList = new CreativeModeTab$ItemDisplayBuilder(this, parameters.enabledFeatures);
      this.displayItemsGenerator.accept(parameters, displayList);
      this.displayItems = displayList.tabContents;
      this.displayItemsSearchTab = displayList.searchTabContents;
   }

   public Collection<ItemStack> getDisplayItems() {
      return this.displayItems;
   }

   public Collection<ItemStack> getSearchTabDisplayItems() {
      return this.displayItemsSearchTab;
   }

   public boolean contains(final ItemStack stack) {
      return this.displayItemsSearchTab.contains(stack);
   }
}
