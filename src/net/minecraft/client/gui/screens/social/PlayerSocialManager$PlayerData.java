package net.minecraft.client.gui.screens.social;

import java.util.UUID;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record PlayerSocialManager$PlayerData(UUID id, String name) {
}
