package net.minecraft.data.tags;

import net.minecraft.core.HolderLookup$Provider;

record TagsProvider$1CombinedData<T>(HolderLookup$Provider contents, TagsProvider$TagLookup<T> parent) {
}
