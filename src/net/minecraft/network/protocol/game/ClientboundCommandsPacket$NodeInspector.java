package net.minecraft.network.protocol.game;

import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public interface ClientboundCommandsPacket$NodeInspector<S> {
   @Nullable
   Identifier suggestionId(ArgumentCommandNode<S, ?> node);

   boolean isExecutable(CommandNode<S> node);

   boolean isRestricted(CommandNode<S> node);
}
