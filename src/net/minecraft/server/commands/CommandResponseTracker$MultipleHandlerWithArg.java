package net.minecraft.server.commands;

@FunctionalInterface
public interface CommandResponseTracker$MultipleHandlerWithArg<Result, Arg> {
   Result create(int elementCount, int totalValue, Arg argument);
}
