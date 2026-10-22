package net.minecraft.world.entity;

import net.minecraft.network.chat.Component;

@FunctionalInterface
public interface Display$TextDisplay$LineSplitter {
   Display$TextDisplay$CachedInfo split(Component input, int width);
}
