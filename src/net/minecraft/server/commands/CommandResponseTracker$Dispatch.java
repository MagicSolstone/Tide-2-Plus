package net.minecraft.server.commands;

public record CommandResponseTracker$Dispatch<Result, Element>(
   CommandResponseTracker$SingleHandler<Result, Element> onSingle, CommandResponseTracker$MultipleHandler<Result> onMultiple
) {
}
