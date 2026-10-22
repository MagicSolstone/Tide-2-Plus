package net.minecraft.server.packs;

import net.minecraft.server.packs.repository.Pack$Position;

public record PackSelectionConfig(boolean required, Pack$Position defaultPosition, boolean fixedPosition) {
}
