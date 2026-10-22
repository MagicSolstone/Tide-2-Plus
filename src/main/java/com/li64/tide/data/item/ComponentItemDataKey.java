package com.li64.tide.data.item;

import com.li64.tide.data.ItemDataKey;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public record ComponentItemDataKey<T>(DataComponentType<T> component) implements ItemDataKey<T> {
    @Override
    public T get(ItemStack stack) {
        return stack.get(component);
    }

    @Override
    public T getOrDefault(ItemStack stack, T defaultValue) {
        return stack.getOrDefault(component, defaultValue);
    }

    @Override
    public Optional<T> getOptional(ItemStack stack) {
        return Optional.ofNullable(this.get(stack));
    }

    @Override
    public void set(ItemStack stack, T value) {
        stack.set(component, value);
    }

    @Override
    public void remove(ItemStack stack) {
        stack.remove(component);
    }

    @Override
    public boolean isPresent(ItemStack stack) {
        return stack.has(component);
    }
}