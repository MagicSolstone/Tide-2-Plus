package net.minecraft.network.chat;

import com.mojang.brigadier.tree.ArgumentCommandNode;

public record SignableCommand$Argument<S>(ArgumentCommandNode<S, ?> node, String value) {
   public String name() {
      return this.node.getName();
   }
}
