package net.minecraft.server.packs.repository;

import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.flag.FeatureFlagSet;

public record Pack$Metadata(Component description, PackCompatibility compatibility, FeatureFlagSet requestedFeatures, List<String> overlays) {
}
