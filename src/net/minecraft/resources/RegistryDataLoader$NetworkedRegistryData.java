package net.minecraft.resources;

import java.util.List;
import net.minecraft.core.RegistrySynchronization$PackedRegistryEntry;
import net.minecraft.tags.TagNetworkSerialization$NetworkPayload;

public record RegistryDataLoader$NetworkedRegistryData(List<RegistrySynchronization$PackedRegistryEntry> elements, TagNetworkSerialization$NetworkPayload tags) {
}
