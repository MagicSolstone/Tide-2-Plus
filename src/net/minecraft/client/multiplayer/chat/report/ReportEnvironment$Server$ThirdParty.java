package net.minecraft.client.multiplayer.chat.report;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record ReportEnvironment$Server$ThirdParty(String ip) implements ReportEnvironment$Server {
}
