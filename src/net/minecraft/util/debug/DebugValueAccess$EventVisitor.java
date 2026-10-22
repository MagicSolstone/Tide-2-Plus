package net.minecraft.util.debug;

@FunctionalInterface
public interface DebugValueAccess$EventVisitor<T> {
   void accept(T value, int remainingTicks, int totalLifetime);
}
