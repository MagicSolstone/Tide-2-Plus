package net.minecraft.server.commands;

@FunctionalInterface
public interface CommandResponseTracker$MultipleHandler<Result> {
   Result create(int elementCount, int totalValue);
}
