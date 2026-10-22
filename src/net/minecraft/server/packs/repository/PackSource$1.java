package net.minecraft.server.packs.repository;

import java.util.function.UnaryOperator;
import net.minecraft.network.chat.Component;

class PackSource$1 implements PackSource {
   PackSource$1(final UnaryOperator val$decorator, final boolean val$addAutomatically) {
      this.val$decorator = val$decoratorx;
      this.val$addAutomatically = val$addAutomaticallyx;
   }

   @Override
   public Component decorate(final Component packDescription) {
      return this.val$decorator.apply(packDescription);
   }

   @Override
   public boolean shouldAddAutomatically() {
      return this.val$addAutomatically;
   }
}
