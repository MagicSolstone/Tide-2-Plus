package net.minecraft.network.chat;

import net.minecraft.world.item.component.ResolvableProfile;

public record FontDescription$PlayerSprite(ResolvableProfile profile, boolean hat) implements FontDescription {
}
