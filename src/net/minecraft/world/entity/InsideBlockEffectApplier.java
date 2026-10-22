package net.minecraft.world.entity;

import java.util.function.Consumer;

public interface InsideBlockEffectApplier {
   InsideBlockEffectApplier NOOP = new InsideBlockEffectApplier$1();

   void apply(InsideBlockEffectType type);

   void runBefore(InsideBlockEffectType type, Consumer<Entity> effect);

   void runAfter(InsideBlockEffectType type, Consumer<Entity> effect);
}
