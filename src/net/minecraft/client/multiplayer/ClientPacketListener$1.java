package net.minecraft.client.multiplayer;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.commands.Commands;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.network.protocol.game.ClientboundCommandsPacket$NodeBuilder;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class ClientPacketListener$1 implements ClientboundCommandsPacket$NodeBuilder<ClientSuggestionProvider> {
   @Override
   public ArgumentBuilder<ClientSuggestionProvider, ?> createLiteral(final String id) {
      return LiteralArgumentBuilder.literal(id);
   }

   @Override
   public ArgumentBuilder<ClientSuggestionProvider, ?> createArgument(
      final String id, final ArgumentType<?> argumentType, @Nullable final Identifier suggestionId
   ) {
      RequiredArgumentBuilder<ClientSuggestionProvider, ?> builder = RequiredArgumentBuilder.argument(id, argumentType);
      if (suggestionId != null) {
         builder.suggests(SuggestionProviders.getProvider(suggestionId));
      }

      return builder;
   }

   @Override
   public ArgumentBuilder<ClientSuggestionProvider, ?> configure(
      final ArgumentBuilder<ClientSuggestionProvider, ?> builder, final boolean executable, final boolean restricted
   ) {
      if (executable) {
         builder.executes(c -> 0);
      }

      if (restricted) {
         builder.requires(Commands.hasPermission(ClientPacketListener.RESTRICTED_COMMAND_CHECK));
      }

      return builder;
   }
}
