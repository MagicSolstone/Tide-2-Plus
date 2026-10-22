package net.minecraft.network.protocol.game;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfo$Template;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

record ClientboundCommandsPacket$ArgumentNodeStub(String id, ArgumentTypeInfo$Template<?> argumentType, @Nullable Identifier suggestionId)
   implements ClientboundCommandsPacket$NodeStub {
   @Override
   public <S> ArgumentBuilder<S, ?> build(final CommandBuildContext context, final ClientboundCommandsPacket$NodeBuilder<S> builder) {
      ArgumentType<?> type = this.argumentType.instantiate(context);
      return builder.createArgument(this.id, type, this.suggestionId);
   }

   @Override
   public void write(final FriendlyByteBuf output) {
      output.writeUtf(this.id);
      serializeCap(output, this.argumentType);
      if (this.suggestionId != null) {
         output.writeIdentifier(this.suggestionId);
      }
   }

   private static <A extends ArgumentType<?>> void serializeCap(final FriendlyByteBuf output, final ArgumentTypeInfo$Template<A> argumentType) {
      serializeCap(output, argumentType.type(), argumentType);
   }

   private static <A extends ArgumentType<?>, T extends ArgumentTypeInfo$Template<A>> void serializeCap(
      final FriendlyByteBuf output, final ArgumentTypeInfo<A, T> info, final ArgumentTypeInfo$Template<A> argumentType
   ) {
      output.writeVarInt(BuiltInRegistries.COMMAND_ARGUMENT_TYPE.getId(info));
      info.serializeToNetwork((T)argumentType, output);
   }
}
