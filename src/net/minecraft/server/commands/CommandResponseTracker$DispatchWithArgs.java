package net.minecraft.server.commands;

public record CommandResponseTracker$DispatchWithArgs<Result, Element, Arg0, Arg1>(
   CommandResponseTracker$SingleHandlerWithArgs<Result, Element, Arg0, Arg1> onSingle,
   CommandResponseTracker$MultipleHandlerWithArgs<Result, Arg0, Arg1> onMultiple
) {
}
