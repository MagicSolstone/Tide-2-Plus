package net.minecraft.server.commands;

@FunctionalInterface
public interface CommandResponseTracker$MultipleHandlerWithArgs<Result, Arg0, Arg1> {
   Result create(int elementCount, int totalValue, Arg0 argument0, Arg1 argument1);
}
