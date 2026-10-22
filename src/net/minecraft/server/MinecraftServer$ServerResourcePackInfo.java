package net.minecraft.server;

import java.util.UUID;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

public record MinecraftServer$ServerResourcePackInfo(UUID id, String url, String hash, boolean isRequired, @Nullable Component prompt) {
}
