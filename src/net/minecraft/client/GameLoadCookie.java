package net.minecraft.client;

import com.mojang.realmsclient.client.RealmsClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.main.GameConfig$QuickPlayData;

@Environment(EnvType.CLIENT)
public record GameLoadCookie(RealmsClient realmsClient, GameConfig$QuickPlayData quickPlayData) {
}
