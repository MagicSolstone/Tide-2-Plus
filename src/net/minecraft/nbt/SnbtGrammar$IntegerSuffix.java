package net.minecraft.nbt;

import org.jspecify.annotations.Nullable;

record SnbtGrammar$IntegerSuffix(@Nullable SnbtGrammar$SignedPrefix signed, @Nullable SnbtGrammar$TypeSuffix type) {
   public static final SnbtGrammar$IntegerSuffix EMPTY = new SnbtGrammar$IntegerSuffix(null, null);
}
