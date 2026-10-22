package net.minecraft.server.commands;

public record CommandResponseTracker$DispatchWithArg<Result, Element, Arg>(
   CommandResponseTracker$SingleHandlerWithArg<Result, Element, Arg> onSingle, CommandResponseTracker$MultipleHandlerWithArg<Result, Arg> onMultiple
) {
}
