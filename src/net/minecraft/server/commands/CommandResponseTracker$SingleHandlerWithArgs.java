package net.minecraft.server.commands;

@FunctionalInterface
public interface CommandResponseTracker$SingleHandlerWithArgs<Result, Element, Arg0, Arg1> {
   Result create(Element element, int totalValue, Arg0 argument0, Arg1 argument1);
}
