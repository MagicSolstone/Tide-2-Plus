package net.minecraft.client.resources.server;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum ServerPackManager$PackPromptStatus {
   PENDING,
   ALLOWED,
   DECLINED;
}
