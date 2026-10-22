package net.minecraft.util.debug;

public interface DebugValueSource$Registration {
   <T> void register(DebugSubscription<T> subscription, DebugValueSource$ValueGetter<T> getter);
}
