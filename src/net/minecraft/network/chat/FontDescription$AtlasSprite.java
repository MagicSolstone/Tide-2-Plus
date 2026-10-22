package net.minecraft.network.chat;

import net.minecraft.resources.Identifier;

public record FontDescription$AtlasSprite(Identifier atlasId, Identifier spriteId) implements FontDescription {
}
