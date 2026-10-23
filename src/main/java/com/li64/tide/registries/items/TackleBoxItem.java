package com.li64.tide.registries.items;

import com.li64.tide.client.gui.menus.AnglingTableMenu;
import com.li64.tide.data.tackle.TackleLog;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * A tackle box: 27 slots for lines, hooks, bobbers, lures and bait, kept in the item. Right-click to open it;
 * sneak + right-click to open a portable angling table and change what's on a rod wherever you are.
 */
public class TackleBoxItem extends AbstractTooltipItem {
    public static final int SIZE = 27;

    public TackleBoxItem(Properties properties) {
        super(properties.stacksTo(1).component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
    }

    @Override
    public @NotNull InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack box = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            if (player.isShiftKeyDown()) {
                player.openMenu(new SimpleMenuProvider((id, inv, p) -> new AnglingTableMenu(id, inv, ContainerLevelAccess.NULL),
                        Component.translatable("block.tide.angling_table")));
            } else {
                player.openMenu(new SimpleMenuProvider((id, inv, p) -> new Menu(id, inv, box), box.getHoverName()));
            }
        }
        level.playSound(null, player.blockPosition(), SoundEvents.CHEST_OPEN, SoundSource.PLAYERS, 0.5f, 1.4f);
        return InteractionResult.SUCCESS;
    }

    @Override
    public void addTooltip(ItemStack stack, Consumer<Component> tooltip) {
        ItemContainerContents contents = stack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
        long used = contents.nonEmptyItemCopyStream().count();
        tooltip.accept(Component.literal(used + " / " + SIZE + " slots used").withStyle(ChatFormatting.GRAY));
        tooltip.accept(Component.literal("Right-click to open").withStyle(ChatFormatting.DARK_GRAY));
        tooltip.accept(Component.literal("Sneak + right-click: portable angling table").withStyle(ChatFormatting.DARK_GRAY));
    }

    /** The box's chest screen (the vanilla 9x3 one, so no new client screen), only taking tackle. */
    public static class Menu extends ChestMenu {
        private final ItemStack box;

        public Menu(int id, Inventory inventory, ItemStack box) {
            super(MenuType.GENERIC_9x3, id, inventory, load(box), 3);
            this.box = box;
            SimpleContainer container = (SimpleContainer) this.getContainer();
            for (int i = 0; i < SIZE; i++) {           // swap in slots that only take tackle
                Slot old = this.slots.get(i);
                Slot only = new Slot(container, old.getContainerSlot(), old.x, old.y) {
                    @Override public boolean mayPlace(@NotNull ItemStack stack) { return TackleLog.fitsTackleBox(stack); }
                };
                only.index = i;
                this.slots.set(i, only);
            }
        }

        private static SimpleContainer load(ItemStack box) {
            SimpleContainer c = new SimpleContainer(SIZE) {
                @Override
                public void setChanged() {
                    super.setChanged();
                    save(box, this);       // every change goes straight into the box item
                }
            };
            NonNullList<ItemStack> items = NonNullList.withSize(SIZE, ItemStack.EMPTY);
            box.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyInto(items);
            for (int i = 0; i < SIZE; i++) c.setItem(i, items.get(i));
            return c;
        }

        private static void save(ItemStack box, SimpleContainer c) {
            box.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(c.getItems()));
        }

        @Override
        public void removed(@NotNull Player player) {
            super.removed(player);
            save(box, (SimpleContainer) this.getContainer());
        }

        @Override
        public boolean stillValid(@NotNull Player player) {
            return player.getMainHandItem() == box || player.getOffhandItem() == box;
        }

        @Override
        public void clicked(int slot, int button, @NotNull ContainerInput input, @NotNull Player player) {
            // the open box itself can't be moved (or it could be emptied twice)
            if (slot >= 0 && slot < this.slots.size() && this.slots.get(slot).getItem() == box) return;
            if (input == ContainerInput.SWAP && (player.getInventory().getItem(button) == box
                    || (button == 40 && player.getOffhandItem() == box))) return;
            super.clicked(slot, button, input, player);
        }
    }
}
