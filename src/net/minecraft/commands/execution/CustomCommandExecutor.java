package net.minecraft.commands.execution;

import com.mojang.brigadier.context.ContextChain;

public interface CustomCommandExecutor<T> {
   void run(T sender, ContextChain<T> currentStep, ChainModifiers modifiers, ExecutionControl<T> output);
}
