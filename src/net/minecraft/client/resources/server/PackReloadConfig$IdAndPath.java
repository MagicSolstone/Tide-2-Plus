package net.minecraft.client.resources.server;

import java.nio.file.Path;
import java.util.UUID;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record PackReloadConfig$IdAndPath(UUID id, Path path) {
}
