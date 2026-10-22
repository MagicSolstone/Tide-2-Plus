package net.minecraft.commands.arguments.selector.options;

import java.util.function.Predicate;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import net.minecraft.network.chat.Component;

record EntitySelectorOptions$Option(EntitySelectorOptions$Modifier modifier, Predicate<EntitySelectorParser> canUse, Component description) {
}
