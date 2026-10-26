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
 * A tackle box: 27 slots for lines, hooks, bobbers, lures and bait and a rack of 5 rods, kept in the item. Right-click
 * to open it: the box, your inventory and a portable angling table, to change what's on a rod wherever you are.
 */
public class TackleBoxItem extends AbstractTooltipItem {
    public static final int SIZE = 27;

    public TackleBoxItem(Properties properties) {
        super(properties.stacksTo(1).component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
    }

    @Override
    public @NotNull InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack box = player.getItemInHand(hand);
        if (!level.isClientSide()) open(player, box, true);      // one screen: the box, your inventory and the table to rig a rod
        level.playSound(null, player.blockPosition(), SoundEvents.CHEST_OPEN, SoundSource.PLAYERS, 0.5f, 1.4f);
        return InteractionResult.SUCCESS;
    }

    /** Opens the box, or its rigging view (an angling table next to the box's tackle). */
    public static void open(Player player, ItemStack box, boolean rig) {
        if (rig) player.openMenu(new SimpleMenuProvider((id, inv, p) -> new RigMenu(id, inv, box), box.getHoverName()));
        else player.openMenu(new SimpleMenuProvider((id, inv, p) -> new Menu(id, inv, box), box.getHoverName()));
    }

    public static final int MAX_RODS = 5;

    /** A tackle slot: lines, hooks, bobbers, lures and bait. */
    static Slot tackleSlot(net.minecraft.world.Container box, int index, int x, int y) {
        return new Slot(box, index, x, y) {
            @Override public boolean mayPlace(@NotNull ItemStack stack) { return TackleLog.fitsTackleBox(stack); }
        };
    }

    /** A slot in the rod rack. */
    static Slot rodSlot(net.minecraft.world.Container box, int index, int x, int y) {
        return new Slot(box, index, x, y) {
            @Override public boolean mayPlace(@NotNull ItemStack stack) { return isRod(stack); }
            @Override public int getMaxStackSize() { return 1; }
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
     * rows (the hotbar stays), so lines, lures and bait go straight from the box onto the rod and back. The player's own
     * inventory rows follow below the hotbar, so everything they carry is in reach too.
     */
    public static class RigMenu extends AnglingTableMenu {
        private final ItemStack box;
        private final SimpleContainer tackle;
        /** Where the box's tackle, the hotbar, the rod rack and the player's own rows start in {@link #slots}. */
        private final int boxStart, hotbarStart, rackStart, ownStart;
        /** Top to bottom: the table, the box's rows, the player's own inventory rows, then the hotbar (menu y). */
        public static final int OWN_Y = 152, HOTBAR_Y = OWN_Y + 3 * 18 + 4;

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
            this.boxStart = first;
            this.hotbarStart = first + SIZE;
            for (int i = 0; i < 9; i++) {      // the hotbar moves to the bottom, under the player's own rows
                Slot old = this.slots.get(hotbarStart + i);
                Slot low = new Slot(inventory, old.getContainerSlot(), old.x, HOTBAR_Y);
                low.index = hotbarStart + i;
                this.slots.set(hotbarStart + i, low);
            }
            this.rackStart = this.slots.size();
            for (int i = 0; i < MAX_RODS; i++) this.addSlot(rodSlot(tackle, SIZE + i, Menu.RACK_X, 84 + i * 18));
            this.ownStart = this.slots.size();
            for (int row = 0; row < 3; row++)
                for (int col = 0; col < 9; col++) this.addSlot(new Slot(inventory, 9 + row * 9 + col, 8 + col * 18, OWN_Y + row * 18));
        }

        /** Shift-click from the player's own rows: onto the rod first, then into the box (or its rack), then the hotbar. */
        @Override
        public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
            if (index < ownStart) return super.quickMoveStack(player, index);
            Slot slot = this.slots.get(index);
            if (!slot.hasItem()) return ItemStack.EMPTY;
            ItemStack stack = slot.getItem(), before = stack.copy();
            if (!this.moveItemStackTo(stack, 0, 5, false)
                    && !(isRod(stack) ? this.moveItemStackTo(stack, rackStart, rackStart + MAX_RODS, false) : this.moveItemStackTo(stack, boxStart, boxStart + SIZE, false))
                    && !this.moveItemStackTo(stack, hotbarStart, hotbarStart + 9, false)) return ItemStack.EMPTY;
            if (stack.isEmpty()) slot.setByPlayer(ItemStack.EMPTY);
            else slot.setChanged();
            return before;
        }

        @Override
        public void removed(@NotNull Player player) {
            // whatever is left on the table goes back in the box if it fits, else to the player
            for (int i = 0; i < this.inputSlots.getContainerSize(); i++) {
                ItemStack left = this.inputSlots.getItem(i);
                if (left.isEmpty()) continue;
                for (int j = 0; j < tackle.getContainerSize() && !left.isEmpty(); j++)       // tackle to tackle slots, a rod to the rack
                    if ((j < SIZE ? TackleLog.fitsTackleBox(left) : isRod(left)) && tackle.getItem(j).isEmpty()) { tackle.setItem(j, left); left = ItemStack.EMPTY; }
                    else if (j < SIZE && ItemStack.isSameItemSameComponents(tackle.getItem(j), left) && tackle.getItem(j).getCount() < tackle.getItem(j).getMaxStackSize()) {
                        int move = Math.min(left.getCount(), tackle.getItem(j).getMaxStackSize() - tackle.getItem(j).getCount());
                        tackle.getItem(j).grow(move); left.shrink(move);
                    }
                this.inputSlots.setItem(i, left);
            }
            super.removed(player);
            Menu.write(box, tackle);
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
        long rods = items.stream().filter(TackleBoxItem::isRod).count();
        tooltip.accept(Component.literal((items.size() - rods) + " / " + SIZE + " tackle slots used · " + rods + " / " + MAX_RODS + " rods").withStyle(ChatFormatting.GRAY));
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
        tooltip.accept(Component.literal("Right-click: the box, your inventory and a table to rig a rod").withStyle(ChatFormatting.DARK_GRAY));
    }

    /** The box's screen: 27 slots of tackle, a rack of 3 for rods beside them, then your inventory. */
    public static class Menu extends HeldStorageMenu {
        public static final int RACK_X = 181;

        /** The client's copy (the box itself stays on the server). */
        public Menu(int id, Inventory inventory) {
            this(id, inventory, ItemStack.EMPTY);
        }

        public Menu(int id, Inventory inventory, ItemStack box) {
            super(com.li64.tide.client.gui.TideMenuTypes.TACKLE_BOX, id, inventory, box, SIZE + MAX_RODS, 8, 85);
        }

        @Override
        protected void addStorageSlots(SimpleContainer box) {
            for (int i = 0; i < SIZE; i++) this.addSlot(tackleSlot(box, i, 8 + (i % 9) * 18, 18 + (i / 9) * 18));
            for (int i = 0; i < MAX_RODS; i++) this.addSlot(rodSlot(box, SIZE + i, RACK_X, 18 + i * 18));
        }

        @Override
        protected void load(ItemStack box, SimpleContainer into) {
            Menu.loadInto(box, into);
        }

        @Override
        protected void save(ItemStack box, SimpleContainer from) {
            Menu.write(box, from);
        }

        static void loadInto(ItemStack box, SimpleContainer c) {
            NonNullList<ItemStack> items = NonNullList.withSize(c.getContainerSize(), ItemStack.EMPTY);
            box.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyInto(items);
            for (int i = 0; i < c.getContainerSize(); i++) c.setItem(i, items.get(i));
        }

        static SimpleContainer load(ItemStack box) {
            SimpleContainer c = new SimpleContainer(SIZE + MAX_RODS) {
                @Override
                public void setChanged() {
                    super.setChanged();
                    write(box, this);
                }
            };
            loadInto(box, c);
            return c;
        }

        static void write(ItemStack box, SimpleContainer c) {
            if (box.isEmpty()) return;
            box.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(c.getItems()));
        }
    }
}
