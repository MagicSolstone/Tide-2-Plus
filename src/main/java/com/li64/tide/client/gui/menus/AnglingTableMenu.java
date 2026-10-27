package com.li64.tide.client.gui.menus;

import com.li64.tide.client.gui.TideMenuTypes;
import com.li64.tide.data.TideTags;
import com.li64.tide.data.rods.CustomRodManager;
import com.li64.tide.registries.blocks.AnglingTableBlock;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class AnglingTableMenu extends ItemCombinerMenu {
    protected Level level;

    public AnglingTableMenu(int menuId, Inventory inventory) {
        this(menuId, inventory, ContainerLevelAccess.NULL);
    }

    public AnglingTableMenu(int menuId, Inventory inventory, ContainerLevelAccess level) {
        this(TideMenuTypes.ANGLING_TABLE, menuId, inventory, level);
    }

    protected AnglingTableMenu(net.minecraft.world.inventory.MenuType<?> type, int menuId, Inventory inventory, ContainerLevelAccess level) {
        super(type, menuId, inventory, level, createInputSlotDefinitions());
        this.level = inventory.player.level();
        // one of each on the rod: the rod, line, bobber, hook and lure slots hold a single item (a dropped or shift-clicked
        // stack leaves the rest in hand), so taking the finished rod never eats the spares
        for (int i = 0; i <= 4; i++) {
            net.minecraft.world.inventory.Slot orig = this.slots.get(i);
            net.minecraft.world.inventory.Slot one = new net.minecraft.world.inventory.Slot(this.inputSlots, orig.getContainerSlot(), orig.x, orig.y) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return orig.mayPlace(stack);
                }

                @Override
                public int getMaxStackSize() {
                    return 1;
                }
            };
            one.index = i;
            this.slots.set(i, one);
        }
    }

    public net.minecraft.world.inventory.ContainerLevelAccess access() {
        return this.access;
    }

    @Override
    protected boolean mayPickup(@NotNull Player player, boolean hasItem) {
        return true;
    }

    @Override
    protected void onTake(Player player, ItemStack stack) {
        level.playLocalSound(player.blockPosition(), SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.BLOCKS, 1.5F, this.level.getRandom().nextFloat() * 0.1F + 0.9F, false);
        this.access.execute((level, pos) -> level.levelEvent(1044, pos, 0));

        stack.onCraftedBy(player, stack.getCount());
        inputSlots.clearContent();
    }

    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);
        // opened from a tackle box: no block, so hand back whatever is still in the slots
        if (this.access == ContainerLevelAccess.NULL && player instanceof net.minecraft.server.level.ServerPlayer)
            this.clearContainer(player, this.inputSlots);
    }

    @Override
    protected boolean isValidBlock(BlockState blockState) {
        return blockState.getBlock() instanceof AnglingTableBlock;
    }

    @Override
    public void createResult() {
        if (!inputSlots.getItem(0).isEmpty()) {
            ItemStack rod = inputSlots.getItem(0);
            if (CustomRodManager.hasLine(rod)) {
                ItemStack line = CustomRodManager.getLine(rod);
                CustomRodManager.setLine(rod, null);
                if (!inputSlots.getItem(1).isEmpty()) {
                    if (!player.level().isClientSide() && !player.addItem(line.copy())) player.drop(line, true, net.minecraft.util.Prediction.SERVER_ONLY);
                }
                else inputSlots.setItem(1, line);
            }
            if (CustomRodManager.hasBobber(rod)) {
                ItemStack bobber = CustomRodManager.getBobber(rod);
                CustomRodManager.setBobber(rod, null);
                if (!inputSlots.getItem(2).isEmpty()) {
                    if (!player.level().isClientSide() && !player.addItem(bobber.copy())) player.drop(bobber, true, net.minecraft.util.Prediction.SERVER_ONLY);
                }
                else inputSlots.setItem(2, bobber);
            }
            if (CustomRodManager.hasLure(rod)) {
                ItemStack lure = CustomRodManager.getLure(rod);
                CustomRodManager.setLure(rod, null);
                if (!inputSlots.getItem(4).isEmpty()) {
                    if (!player.level().isClientSide() && !player.addItem(lure.copy())) player.drop(lure, true, net.minecraft.util.Prediction.SERVER_ONLY);
                }
                else inputSlots.setItem(4, lure);
            }
            if (CustomRodManager.hasHook(rod)) {
                ItemStack hook = CustomRodManager.getHook(rod);
                CustomRodManager.setHook(rod, null);
                if (!inputSlots.getItem(3).isEmpty()) {
                    if (!player.level().isClientSide() && !player.addItem(hook.copy())) player.drop(hook, true, net.minecraft.util.Prediction.SERVER_ONLY);
                }
                else inputSlots.setItem(3, hook);
            }
        }

        if (inputSlots.getItem(0).isEmpty() ||
                (inputSlots.getItem(1).isEmpty()
                && inputSlots.getItem(2).isEmpty()
                && inputSlots.getItem(3).isEmpty()
                && inputSlots.getItem(4).isEmpty()))
            this.resultSlots.setItem(0, ItemStack.EMPTY);
        else {
            ItemStack rod = inputSlots.getItem(0);
            ItemStack line = inputSlots.getItem(1);
            ItemStack bobber = inputSlots.getItem(2);
            ItemStack hook = inputSlots.getItem(3);
            ItemStack lure = inputSlots.getItem(4);

            ItemStack out = getOutputRodFor(rod, bobber, hook, line);
            if (!lure.isEmpty()) CustomRodManager.setLure(out, lure);
            this.resultSlots.setItem(0, out);
        }
    }

    public ItemStack getOutputRodFor(ItemStack input, ItemStack bobberItem, ItemStack hookItem, ItemStack lineItem) {
        ItemStack newRod = input.copy();
        if (!lineItem.isEmpty()) CustomRodManager.setLine(newRod, lineItem);
        if (!bobberItem.isEmpty()) CustomRodManager.setBobber(newRod, bobberItem);
        if (!hookItem.isEmpty()) CustomRodManager.setHook(newRod, hookItem);
        return newRod;
    }

    private static @NotNull ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
        return ItemCombinerMenuSlotDefinition.create()
            .withSlot(0, 26, 11,
                    stack -> stack.is(TideTags.Items.FISHING_RODS))
            .withSlot(1, 134, 8,
                    stack -> stack.is(TideTags.Items.LINES))
            .withSlot(2, 134, 32,
                    stack -> stack.is(TideTags.Items.BOBBERS))
            .withSlot(3, 134, 56,
                    stack -> stack.is(TideTags.Items.HOOKS))
            .withSlot(4, 110, 56,
                    stack -> stack.is(TideTags.Items.LURES))
            .withResultSlot(5, 26, 49).build();
    }
}
