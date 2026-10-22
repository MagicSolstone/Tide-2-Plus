package net.minecraft.world.entity;

import net.minecraft.network.chat.Component;

public record Display$TextDisplay$TextRenderState(
   Component text, int lineWidth, Display$IntInterpolator textOpacity, Display$IntInterpolator backgroundColor, byte flags
) {
}
