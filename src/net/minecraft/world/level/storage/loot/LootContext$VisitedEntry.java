package net.minecraft.world.level.storage.loot;

public record LootContext$VisitedEntry<T extends Validatable>(LootDataType<T> type, T value) {
}
