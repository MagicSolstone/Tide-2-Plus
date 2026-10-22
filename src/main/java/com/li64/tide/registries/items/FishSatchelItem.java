package com.li64.tide.registries.items;

import net.minecraft.world.InteractionResult;
import com.li64.tide.data.fishing.FishData;
import com.li64.tide.data.item.SatchelContents;
import com.li64.tide.data.TideTags;
import com.li64.tide.data.item.TideItemData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.BundleContents;
import java.util.List;

public class FishSatchelItem extends AbstractTooltipItem {
    private static final int BAR_COLOR = 0x6666FF;

    public FishSatchelItem(Item.Properties properties) {
        super(properties);
    }

    public static boolean canPutInSatchel(ItemStack stack) {
        return stack.is(TideTags.Items.FISH) || FishData.get(stack).isPresent();
    }

    public static int getFishCount(ItemStack stack) {
        SatchelContents contents = TideItemData.SATCHEL_CONTENTS.get(stack);
        if (contents == null) return 0;
        return contents.size();
    }

    public static int getRemainingSlots(ItemStack stack) {
        return SatchelContents.MAX_STACKS - getFishCount(stack);
    }

    @Override
    public @NotNull InteractionResult use(Level level, Player player, InteractionHand used) {
        ItemStack satchel = player.getItemInHand(used);
        TideItemData.FISH_SATCHEL_OPENED.set(satchel, !TideItemData.FISH_SATCHEL_OPENED.getOrDefault(satchel, false));
        player.playSound(SoundEvents.ARMOR_EQUIP_LEATHER.value(), 0.8f, 0.8f + player.level().getRandom().nextFloat() * 0.4f);
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player) {
        if (action != ClickAction.SECONDARY) return false;
        SatchelContents contents = TideItemData.SATCHEL_CONTENTS.getOrDefault(stack, new SatchelContents());
        SatchelContents.Mutable mutable = new SatchelContents.Mutable(contents);
        ItemStack slotStack = slot.getItem();

        if (slotStack.isEmpty() && !mutable.isEmpty()) {
            // place next stack
            ItemStack removed = mutable.removeStack();
            if (removed != null && !removed.isEmpty()) {
                this.playRemoveOneSound(player);
                mutable.tryInsert(slot.safeInsert(removed));
            }
            else return false;
        }
        else if (!slotStack.isEmpty()
                && slotStack.getItem().canFitInsideContainerItems()
                && canPutInSatchel(slotStack)) {
            if (mutable.tryTransfer(slot, player)) this.playInsertSound(player);
            else return false;
        }
        else return false;

        TideItemData.SATCHEL_CONTENTS.set(stack, mutable.toImmutable());
        return true;
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if (action != ClickAction.SECONDARY || !slot.allowModification(player)) return false;
        return overrideOtherStackedOnMe(stack, other, player, access, true);
    }

    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Player player, SlotAccess access, boolean playSound) {
        SatchelContents contents = TideItemData.SATCHEL_CONTENTS.getOrDefault(stack, new SatchelContents());
        SatchelContents.Mutable mutable = new SatchelContents.Mutable(contents);

        if (other.isEmpty()) {
            // try pull next stack
            ItemStack removedStack = mutable.removeStack();
            if (removedStack != null && !removedStack.isEmpty()) {
                if (playSound) this.playRemoveOneSound(player);
                access.set(removedStack);
            }
            else return false;
        }
        else if (other.getItem().canFitInsideContainerItems() && canPutInSatchel(other)) {
            // insert stack
            if (mutable.tryInsert(other)) {
                if (playSound) this.playInsertSound(player);
            }
            else return false;
        }
        else return false;

        TideItemData.SATCHEL_CONTENTS.set(stack, mutable.toImmutable());
        return true;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        SatchelContents contents = TideItemData.SATCHEL_CONTENTS.get(stack);
        return contents != null && !contents.isEmpty();
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        SatchelContents contents = TideItemData.SATCHEL_CONTENTS.get(stack);
        return Math.min(1 + (12 * contents.size()) / SatchelContents.MAX_STACKS, 13);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return BAR_COLOR;
    }


    @Override
    public @NotNull Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        return !stack.getOrDefault(DataComponents.TOOLTIP_DISPLAY, net.minecraft.world.item.component.TooltipDisplay.DEFAULT).hideTooltip()
                ? Optional.ofNullable(TideItemData.SATCHEL_CONTENTS.getOrDefault(stack, new SatchelContents()))
                .map(contents -> new BundleTooltip(new BundleContents(contents.items().stream().filter(i -> !i.isEmpty()).map(net.minecraft.world.item.ItemStackTemplate::fromNonEmptyStack).toList())))
                : Optional.empty();
    }

    @Override
    public void onDestroyed(ItemEntity entity) {
        SatchelContents contents = TideItemData.SATCHEL_CONTENTS.get(entity.getItem());
        if (contents == null) return;
        TideItemData.SATCHEL_CONTENTS.set(entity.getItem(), new SatchelContents());
        ItemUtils.onContainerDestroyed(entity, contents.items().stream().map(ItemStack::copy));
    }

    private void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8f, 0.8f + entity.level().getRandom().nextFloat() * 0.4f);
    }

    private void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8f, 0.8f + entity.level().getRandom().nextFloat() * 0.4f);
    }

    @Override
    public void addTooltip(ItemStack stack, Consumer<Component> tooltip) {
        Style gray = Component.empty().getStyle().withColor(ChatFormatting.GRAY);
        SatchelContents contents = TideItemData.SATCHEL_CONTENTS.get(stack);
        if (contents != null) tooltip.accept(Component.translatable("item.minecraft.bundle.fullness",
                contents.size(), SatchelContents.MAX_STACKS).withStyle(ChatFormatting.GRAY));
        tooltip.accept(Component.translatable("item.tide.fish_satchel.desc_0").setStyle(gray));
        tooltip.accept(Component.translatable("item.tide.fish_satchel.desc_1").setStyle(gray));
    }
}