package net.minecraft.server.commands;

@FunctionalInterface
public interface ArgProvider$Factory<T> {
   ArgProvider<T> create(String arg);
}
