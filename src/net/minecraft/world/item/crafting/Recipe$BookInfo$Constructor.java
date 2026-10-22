package net.minecraft.world.item.crafting;

import java.util.function.BiFunction;

@FunctionalInterface
public interface Recipe$BookInfo$Constructor<CategoryType, SelfType extends Recipe$BookInfo<CategoryType>> extends BiFunction<CategoryType, String, SelfType> {
}
