package net.minecraft.server.commands;

@FunctionalInterface
public interface CommandResponseTracker$SingleHandler<Result, Element> {
   Result create(Element element, int totalValue);
}
