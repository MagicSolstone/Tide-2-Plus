package net.minecraft.world.level.storage.loot;

@FunctionalInterface
interface IntLimit$IntLimiter {
   int apply(LootContext context, int value);
}
