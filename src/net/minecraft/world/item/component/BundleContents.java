package net.minecraft.world.item.component;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity$Occupant;
import org.apache.commons.lang3.math.Fraction;
import org.jspecify.annotations.Nullable;

public final class BundleContents implements ContainerComponent<BundleContents>, TooltipComponent {
   public static final BundleContents EMPTY = new BundleContents(List.of());
   public static final Codec<BundleContents> CODEC = ItemStackTemplate.CODEC.listOf().xmap(BundleContents::new, contents -> contents.items);
   public static final StreamCodec<RegistryFriendlyByteBuf, BundleContents> STREAM_CODEC = ItemStackTemplate.STREAM_CODEC
      .apply(ByteBufCodecs.list())
      .map(BundleContents::new, contents -> contents.items);
   private static final Fraction BUNDLE_IN_BUNDLE_WEIGHT = Fraction.getFraction(1, 16);
   private static final int NO_STACK_INDEX = -1;
   public static final int NO_SELECTED_ITEM_INDEX = -1;
   public static final DataResult<Fraction> BEEHIVE_WEIGHT = DataResult.success(Fraction.ONE);
   private final List<ItemStackTemplate> items;
   private final int selectedItem;
   private final Supplier<DataResult<Fraction>> weight;

   private BundleContents(final List<ItemStackTemplate> items, final int selectedItem) {
      this.items = items;
      this.selectedItem = selectedItem;
      this.weight = Suppliers.memoize(() -> computeContentWeight(this.items));
   }

   public BundleContents(final List<ItemStackTemplate> items) {
      this(items, -1);
   }

   private static DataResult<Fraction> computeContentWeight(final List<? extends ItemInstance> items) {
      try {
         Fraction weight = Fraction.ZERO;

         for (ItemInstance stack : items) {
            DataResult<Fraction> itemWeight = getWeight(stack);
            if (itemWeight.isError()) {
               return itemWeight;
            }

            weight = weight.add(((Fraction)itemWeight.getOrThrow()).multiplyBy(Fraction.getFraction(stack.count(), 1)));
         }

         return DataResult.success(weight);
      } catch (ArithmeticException var5) {
         return DataResult.error(() -> "Excessive total bundle weight");
      }
   }

   private static DataResult<Fraction> getWeight(final ItemInstance item) {
      BundleContents bundle = item.get(DataComponents.BUNDLE_CONTENTS);
      if (bundle != null) {
         return bundle.weight().map(nestedWeight -> nestedWeight.add(BUNDLE_IN_BUNDLE_WEIGHT));
      } else {
         List<BeehiveBlockEntity$Occupant> bees = item.getOrDefault(DataComponents.BEES, Bees.EMPTY).bees();
         return !bees.isEmpty() ? BEEHIVE_WEIGHT : DataResult.success(Fraction.getFraction(1, item.getMaxStackSize()));
      }
   }

   public static boolean canItemBeInBundle(final ItemStack itemToAdd) {
      return !itemToAdd.isEmpty() && itemToAdd.getItem().canFitInsideContainerItems();
   }

   public int getNumberOfItemsToShow() {
      int numberOfItemStacks = this.size();
      int availableItemsToShow = numberOfItemStacks > 12 ? 11 : 12;
      int itemsOnNonFullRow = numberOfItemStacks % 4;
      int emptySpaceOnNonFullRow = itemsOnNonFullRow == 0 ? 0 : 4 - itemsOnNonFullRow;
      return Math.min(numberOfItemStacks, availableItemsToShow - emptySpaceOnNonFullRow);
   }

   @Override
   public Stream<ItemStack> itemCopies() {
      return this.items.stream().map(ItemStackTemplate::create);
   }

   public List<ItemStackTemplate> items() {
      return this.items;
   }

   @Override
   public int size() {
      return this.items.size();
   }

   public DataResult<Fraction> weight() {
      return this.weight.get();
   }

   public boolean isEmpty() {
      return this.items.isEmpty();
   }

   public int getSelectedItemIndex() {
      return this.selectedItem;
   }

   @Nullable
   public ItemStackTemplate getSelectedItem() {
      return this.selectedItem == -1 ? null : this.items.get(this.selectedItem);
   }

   public BundleContents copyWithContents(final Stream<ItemStack> newContents) {
      BundleContents$Mutable builder = new BundleContents$Mutable();
      newContents.forEach(builder::tryInsert);
      return builder.toImmutable();
   }

   public BundleContents$Mutable asMutable() {
      DataResult<Fraction> currentWeight = this.weight.get();
      if (currentWeight.isError()) {
         return new BundleContents$Mutable();
      } else {
         List<ItemStack> itemsList = new ArrayList<>(this.items.size());

         for (ItemStackTemplate item : this.items) {
            itemsList.add(item.create());
         }

         return new BundleContents$Mutable(itemsList, (Fraction)currentWeight.getOrThrow(), this.selectedItem);
      }
   }

   @Override
   public boolean equals(final Object obj) {
      if (this == obj) {
         return true;
      } else {
         return obj instanceof BundleContents contents ? this.items.equals(contents.items) : false;
      }
   }

   @Override
   public int hashCode() {
      return this.items.hashCode();
   }

   @Override
   public String toString() {
      return "BundleContents" + this.items;
   }
}
