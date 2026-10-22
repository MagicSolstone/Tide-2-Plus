package net.minecraft.server.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.jspecify.annotations.Nullable;

@FunctionalInterface
interface FillCommand$NullableCommandFunction<T, R> {
   @Nullable
   R apply(T t) throws CommandSyntaxException;
}
