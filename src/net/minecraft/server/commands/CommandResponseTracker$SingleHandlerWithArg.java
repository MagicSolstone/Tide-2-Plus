package net.minecraft.server.commands;

@FunctionalInterface
public interface CommandResponseTracker$SingleHandlerWithArg<Result, Element, Arg> {
   Result create(Element element, int totalValue, Arg argument);
}
