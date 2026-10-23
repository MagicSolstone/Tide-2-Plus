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

import java.util.List;
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
        if (!level.isClientSide()) open(player, box, player.isShiftKeyDown());
        level.playSound(null, player.blockPosition(), SoundEvents.CHEST_OPEN, SoundSource.PLAYERS, 0.5f, 1.4f);
        return InteractionResult.SUCCESS;
    }

    /** Opens the box, or its rigging view (an angling table next to the box's tackle). */
    public static void open(Player player, ItemStack box, boolean rig) {
        if (rig) player.openMenu(new SimpleMenuProvider((id, inv, p) -> new RigMenu(id, inv, box), Component.translatable("block.tide.angling_table")));
        else player.openMenu(new SimpleMenuProvider((id, inv, p) -> new Menu(id, inv, box), box.getHoverName()));
    }

    public static final int MAX_RODS = 3;

    /** A box slot: any tackle, or a fishing rod while the box holds fewer than three. */
    static Slot tackleSlot(net.minecraft.world.Container box, int index, int x, int y) {
        return new Slot(box, index, x, y) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                if (TackleLog.fitsTackleBox(stack)) return true;
                if (!isRod(stack)) return false;
                int rods = 0;
                for (int i = 0; i < box.getContainerSize(); i++)
                    if (i != this.getContainerSlot() && isRod(box.getItem(i))) rods++;
                return rods < MAX_RODS;
            }
        };
    }

    static boolean isRod(ItemStack s) {
        return s.getItem() instanceof TideFishingRodItem || s.is(net.minecraft.world.item.Items.FISHING_ROD);
    }

    /** The tackle box the player is holding, if any (main hand first). */
    public static ItemStack held(Player player) {
        if (player.getMainHandItem().getItem() instanceof TackleBoxItem) return player.getMainHandItem();
        if (player.getOffhandItem().getItem() instanceof TackleBoxItem) return player.getOffhandItem();
        return ItemStack.EMPTY;
    }

    /**
     * Rigging a rod from the box: an angling table where the box's 27 slots stand in for the player's inventory
     * rows (the hotbar stays), so lines, lures and bait go straight from the box onto the rod and back.
     */
    public static class RigMenu extends AnglingTableMenu {
        private final ItemStack box;
        private final SimpleContainer tackle;

        public RigMenu(int id, Inventory inventory) {
            this(id, inventory, ItemStack.EMPTY);
        }

        public RigMenu(int id, Inventory inventory, ItemStack box) {
            super(com.li64.tide.client.gui.TideMenuTypes.TACKLE_RIG, id, inventory, ContainerLevelAccess.NULL);
            this.box = box;
            this.tackle = Menu.load(box);
            int first = this.slots.size() - 36;        // the inventory rows come before the hotbar
            for (int i = 0; i < SIZE; i++) {
                Slot old = this.slots.get(first + i);
                Slot only = tackleSlot(tackle, i, old.x, old.y);
                only.index = first + i;
                this.slots.set(first + i, only);
            }
        }

        @Override
        public void removed(@NotNull Player player) {
            // whatever is left on the table goes back in the box if it fits, else to the player
            for (int i = 0; i < this.inputSlots.getContainerSize(); i++) {
                ItemStack left = this.inputSlots.getItem(i);
                if (!left.isEmpty() && TackleLog.fitsTackleBox(left)) this.inputSlots.setItem(i, tackle.addItem(left));
            }
            super.removed(player);
            Menu.save(box, tackle);
        }

        @Override
        public boolean stillValid(@NotNull Player player) {
            return box.isEmpty() || player.getMainHandItem() == box || player.getOffhandItem() == box;
        }

        @Override
        public void clicked(int slot, int button, @NotNull ContainerInput input, @NotNull Player player) {
            if (!box.isEmpty() && slot >= 0 && slot < this.slots.size() && this.slots.get(slot).getItem() == box) return;
            if (!box.isEmpty() && input == ContainerInput.SWAP && (player.getInventory().getItem(button) == box
                    || (button == 40 && player.getOffhandItem() == box))) return;
            super.clicked(slot, button, input, player);
        }
    }

    @Override
    public void addTooltip(ItemStack stack, Consumer<Component> tooltip) {
        ItemContainerContents contents = stack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
        List<ItemStack> items = contents.nonEmptyItemCopyStream().toList();
        tooltip.accept(Component.literal(items.size() + " / " + SIZE + " slots used").withStyle(ChatFormatting.GRAY));
        // the rods by name, then how much of each kind of tackle
        for (ItemStack s : items) if (isRod(s)) tooltip.accept(Component.literal("  ").append(s.getHoverName()).withStyle(ChatFormatting.GOLD));
        java.util.Map<TackleLog.Category, Integer> counts = new java.util.EnumMap<>(TackleLog.Category.class);
        for (ItemStack s : items) {
            TackleLog.Category c = isRod(s) ? null : TackleLog.category(s.getItem());
            if (c != null) counts.merge(c, s.getCount(), Integer::sum);
        }
        if (!counts.isEmpty()) tooltip.accept(Component.literal("  " + counts.entrySet().stream()
                .map(e -> e.getValue() + " " + e.getKey().label.toLowerCase()).collect(java.util.stream.Collectors.joining(", "))).withStyle(ChatFormatting.GRAY));
        tooltip.accept(Component.literal("Right-click to open").withStyle(ChatFormatting.DARK_GRAY));
        tooltip.accept(Component.literal("Sneak + right-click: rig a rod from the box").withStyle(ChatFormatting.DARK_GRAY));
    }

    /** The box's chest screen (the vanilla 9x3 one, so no new client screen), only taking tackle. */
    public static class Menu extends ChestMenu {
        private final ItemStack box;

        /** The client's copy (the box itself stays on the server). */
        public Menu(int id, Inventory inventory) {
            this(id, inventory, ItemStack.EMPTY);
        }

        public Menu(int id, Inventory inventory, ItemStack box) {
            super(com.li64.tide.client.gui.TideMenuTypes.TACKLE_BOX, id, inventory, load(box), 3);
            this.box = box;
            SimpleContainer container = (SimpleContainer) this.getContainer();
            for (int i = 0; i < SIZE; i++) {           // swap in slots that only take tackle
                Slot old = this.slots.get(i);
                Slot only = tackleSlot(container, old.getContainerSlot(), old.x, old.y);
                only.index = i;
                this.slots.set(i, only);
            }
        }

        static SimpleContainer load(ItemStack box) {
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

        static void save(ItemStack box, SimpleContainer c) {
            if (box.isEmpty()) return;
            box.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(c.getItems()));
        }

        @Override
        public void removed(@NotNull Player player) {
            super.removed(player);
            save(box, (SimpleContainer) this.getContainer());
        }

        @Override
        public boolean stillValid(@NotNull Player player) {
            return box.isEmpty() || player.getMainHandItem() == box || player.getOffhandItem() == box;
        }

        @Override
        public void clicked(int slot, int button, @NotNull ContainerInput input, @NotNull Player player) {
            // the open box itself can't be moved (or it could be emptied twice)
            if (!box.isEmpty() && slot >= 0 && slot < this.slots.size() && this.slots.get(slot).getItem() == box) return;
            if (!box.isEmpty() && input == ContainerInput.SWAP && (player.getInventory().getItem(button) == box
                    || (button == 40 && player.getOffhandItem() == box))) return;
            super.clicked(slot, button, input, player);
        }
    }
}
