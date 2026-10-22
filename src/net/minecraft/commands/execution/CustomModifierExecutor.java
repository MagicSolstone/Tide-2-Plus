package net.minecraft.commands.execution;

import com.mojang.brigadier.context.ContextChain;
import java.util.List;

public interface CustomModifierExecutor<T> {
   void apply(T originalSource, List<T> currentSources, ContextChain<T> currentStep, ChainModifiers modifiers, ExecutionControl<T> output);
}
