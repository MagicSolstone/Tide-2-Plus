package net.minecraft.server.commands;

import net.minecraft.resources.Identifier;

public interface FunctionCommand$Callbacks<T> {
   void signalResult(T originalSource, Identifier functionId, int newValue);
}
