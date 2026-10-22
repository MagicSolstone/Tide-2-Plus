package net.minecraft.server;

import net.minecraft.core.RegistryAccess$Frozen;

public record WorldLoader$DataLoadOutput<D>(D cookie, RegistryAccess$Frozen finalDimensions) {
}
