package net.minecraft.client.resources.server;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
enum ServerPackManager$ActivationStatus {
   INACTIVE,
   PENDING,
   ACTIVE;
}
