package net.minecraft.client.resources.model;

import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.item.ClientItem;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public record ClientItemInfoLoader$LoadedClientInfos(Map<Identifier, ClientItem> contents) {
}
