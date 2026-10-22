package net.minecraft.network.protocol.game;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public interface ClientboundCommandsPacket$NodeBuilder<S> {
   ArgumentBuilder<S, ?> createLiteral(String id);

   ArgumentBuilder<S, ?> createArgument(String id, ArgumentType<?> argumentType, @Nullable Identifier suggestionId);

   ArgumentBuilder<S, ?> configure(ArgumentBuilder<S, ?> input, boolean executable, boolean restricted);
}
