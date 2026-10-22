package net.minecraft.world.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData$Builder;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

public class Display$ItemDisplay extends Display {
   private static final String TAG_ITEM = "item";
   private static final String TAG_ITEM_DISPLAY = "item_display";
   private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK_ID = SynchedEntityData.defineId(
      Display$ItemDisplay.class, EntityDataSerializers.ITEM_STACK
   );
   private static final EntityDataAccessor<Byte> DATA_ITEM_DISPLAY_ID = SynchedEntityData.defineId(Display$ItemDisplay.class, EntityDataSerializers.BYTE);
   private final SlotAccess slot = SlotAccess.of(this::getItemStack, this::setItemStack);
   @Nullable
   private Display$ItemDisplay$ItemRenderState itemRenderState;

   public Display$ItemDisplay(final EntityType<?> type, final Level level) {
      super(type, level);
   }

   @Override
   protected void defineSynchedData(final SynchedEntityData$Builder entityData) {
      super.defineSynchedData(entityData);
      entityData.define(DATA_ITEM_STACK_ID, ItemStack.EMPTY);
      entityData.define(DATA_ITEM_DISPLAY_ID, ItemDisplayContext.NONE.getId());
   }

   @Override
   public void onSyncedDataUpdated(final EntityDataAccessor<?> accessor) {
      super.onSyncedDataUpdated(accessor);
      if (DATA_ITEM_STACK_ID.equals(accessor) || DATA_ITEM_DISPLAY_ID.equals(accessor)) {
         this.updateRenderState = true;
      }
   }

   private ItemStack getItemStack() {
      return this.entityData.get(DATA_ITEM_STACK_ID);
   }

   private void setItemStack(final ItemStack item) {
      this.entityData.set(DATA_ITEM_STACK_ID, item);
   }

   private void setItemTransform(final ItemDisplayContext transform) {
      this.entityData.set(DATA_ITEM_DISPLAY_ID, transform.getId());
   }

   private ItemDisplayContext getItemTransform() {
      return ItemDisplayContext.BY_ID.apply(this.entityData.get(DATA_ITEM_DISPLAY_ID));
   }

   @Override
   protected void readAdditionalSaveData(final ValueInput input) {
      super.readAdditionalSaveData(input);
      this.setItemStack(input.<ItemStack>read("item", ItemStack.CODEC).orElse(ItemStack.EMPTY));
      this.setItemTransform(input.<ItemDisplayContext>read("item_display", ItemDisplayContext.CODEC).orElse(ItemDisplayContext.NONE));
   }

   @Override
   protected void addAdditionalSaveData(final ValueOutput output) {
      super.addAdditionalSaveData(output);
      ItemStack itemStack = this.getItemStack();
      if (!itemStack.isEmpty()) {
         output.store("item", ItemStack.CODEC, itemStack);
      }

      output.store("item_display", ItemDisplayContext.CODEC, this.getItemTransform());
   }

   @Nullable
   @Override
   public SlotAccess getSlot(final int slot) {
      return slot == 0 ? this.slot : null;
   }

   @Nullable
   public Display$ItemDisplay$ItemRenderState itemRenderState() {
      return this.itemRenderState;
   }

   @Override
   protected void updateRenderSubState(final boolean shouldInterpolate, final float progress) {
      this.itemRenderState = new Display$ItemDisplay$ItemRenderState(this.getItemStack(), this.getItemTransform());
   }
}
