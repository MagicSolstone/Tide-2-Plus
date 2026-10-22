package net.minecraft.server.packs.resources;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;

public final class PreparableReloadListener$SharedState {
   private final ResourceManager manager;
   private final Map<PreparableReloadListener$StateKey<?>, Object> state = new IdentityHashMap<>();

   public PreparableReloadListener$SharedState(final ResourceManager manager) {
      this.manager = manager;
   }

   public ResourceManager resourceManager() {
      return this.manager;
   }

   public <T> void set(final PreparableReloadListener$StateKey<T> key, final T value) {
      this.state.put(key, value);
   }

   public <T> T get(final PreparableReloadListener$StateKey<T> key) {
      return Objects.requireNonNull((T)this.state.get(key));
   }
}
