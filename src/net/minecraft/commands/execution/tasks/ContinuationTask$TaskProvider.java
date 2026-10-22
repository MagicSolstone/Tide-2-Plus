package net.minecraft.commands.execution.tasks;

import net.minecraft.commands.execution.CommandQueueEntry;
import net.minecraft.commands.execution.Frame;

@FunctionalInterface
public interface ContinuationTask$TaskProvider<T, P> {
   CommandQueueEntry<T> create(Frame frame, P argument);
}
